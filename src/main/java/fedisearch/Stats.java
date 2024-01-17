package fedisearch;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Stats(long instances, long users) {
}
