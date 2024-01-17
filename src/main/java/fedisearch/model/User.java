package fedisearch.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import io.r2dbc.postgresql.codec.Vector;

@Serdeable
@MappedEntity("embeddings")
public record User(
        @GeneratedValue
        @Id
        Long id,
        String username,
        String acct,
        String instance,
        String note,
        Vector embedding
) {
}
