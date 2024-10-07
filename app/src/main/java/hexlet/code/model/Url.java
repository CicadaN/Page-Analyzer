package hexlet.code.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Url {
    private long id;
    private String url;
    private Timestamp createdAt;
}
