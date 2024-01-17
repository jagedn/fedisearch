package fedisearch.model;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import io.r2dbc.postgresql.codec.Vector;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface UserRepository extends ReactiveStreamsCrudRepository<User, Long> {

    @Query("select count(distinct instance) from embeddings")
    Mono<Long> countInstances();

    @Query("select * from embeddings order by (embedding <=> :search) limit :limit")
    Flux<User>findSimilarTo(Vector search, Integer limit);

    @Query("select instance from embeddings where instance is not null order by random() limit :limit")
    Flux<String>randomInstances(Integer limit);

}
