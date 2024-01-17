package fedisearch;


import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import fedisearch.model.User;
import fedisearch.model.UserRepository;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.r2dbc.postgresql.codec.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Controller("/api")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private final UserRepository userRepository;
    private final AllMiniLmL6V2EmbeddingModel embeddingModel;

    public SearchController(UserRepository userRepository, AllMiniLmL6V2EmbeddingModel embeddingModel) {
        this.userRepository = userRepository;
        this.embeddingModel = embeddingModel;
    }


    @Get("stats")
    Flux<Stats>stats(){
        return Flux.zip(userRepository.countInstances(), userRepository.count()).map( i ->
                new Stats(i.getT1(), i.getT2())
        );
    }

    @Post("search")
    Flux<SearchResult>search(String search, @Parameter("max")Optional<Integer>max){
        logger.info("Search {}", search);
        var embedding = embeddingModel.embed(search).content().vector();
        return userRepository
                .findSimilarTo(Vector.of(embedding), Math.min(max.orElse(20), 20))
                .map(u->fromUser(u));
    }

    static SearchResult fromUser(User user){
        return new SearchResult(user.username(), user.acct(), user.instance(), user.note());
    }

}
