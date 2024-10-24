package hexlet.code.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
public final class Url {
    private int id;
    @NonNull private String name;
    private Instant createdAt;
}
