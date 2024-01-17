package fedisearch;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SearchResult(String username,
                           String acct,
                           String instance,
                           String note
                           ){
}
