package fedisearch.jobs;

import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import fedisearch.client.FediUser;
import fedisearch.client.Fediverse;
import fedisearch.model.User;
import fedisearch.model.UserRepository;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import io.r2dbc.postgresql.codec.Vector;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Singleton
public class CronTasks {

    private static final Logger logger = LoggerFactory.getLogger(CronTasks.class.getName());
    private final UserRepository userRepository;
    private final Fediverse fediverse;

    private final AllMiniLmL6V2EmbeddingModel embeddingModel;

    private final String initialDomain;

    public CronTasks(UserRepository userRepository,
                     Fediverse fediverse,
                     AllMiniLmL6V2EmbeddingModel embeddingModel,
                     @Value("fedisearch.initial")String initialDomain) {
        this.userRepository = userRepository;
        this.fediverse = fediverse;
        this.embeddingModel = embeddingModel;
        this.initialDomain = initialDomain;
    }

    @Scheduled(fixedDelay = "${fedisearch.scan-every}")
    void scanForUsers() {
        Flux.from(userRepository.count())
                .flatMap(c->{
                    if( c == 0){
                        return Flux.<String>fromArray(new String[]{initialDomain});
                    }else{
                        return userRepository.randomInstances(10);
                    }
                })
                .distinct()
                .flatMap(this::retrieveUsers)
                .flatMap(s->Mono.just(processUsers(s)))
                .flatMap(userRepository::save)
                .onErrorContinue((t,o)->logger.error("Error retrieving users",t))
                .publishOn(Schedulers.boundedElastic())
                .subscribe();
    }

    Flux<FediUser> retrieveUsers(String instance){
        return fediverse.getUsers("https://%s".formatted(instance));
    }

    User processUsers(FediUser user){
        var description = user.acct().length() > 1 ? user.acct() : user.display_name();
        var embedding = embeddingModel.embed(description).content();
        var instance = user.acct().split("@").length == 2 ? user.acct().split("@")[1] : "";
        return new User(null,user.username(), user.acct(), instance, user.note(), Vector.of(embedding.vector()));
    }

}
