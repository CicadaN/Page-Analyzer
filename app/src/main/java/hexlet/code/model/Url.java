package hexlet.code.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class Url {
    private int id;
    @NonNull
    private String name;
    private Instant createdAt;

    public String getFormattedCreatedAt() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(this.createdAt);
    }
}
