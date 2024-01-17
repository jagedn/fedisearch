package fedisearch.client;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record FediUser(String username, String display_name, String note, String acct, String url) {
}
