package fedisearch;

import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class EmbeddingConfiguration {

    @Bean
    AllMiniLmL6V2EmbeddingModel embeddingModel(){
        return new AllMiniLmL6V2EmbeddingModel();
    }

}
