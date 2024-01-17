package fedisearch.client;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;

@Client("default")
public interface Fediverse {

    @Get("{+path}/api/v1/directory")
    Flux<FediUser> getUsers(@PathVariable String path);

}
