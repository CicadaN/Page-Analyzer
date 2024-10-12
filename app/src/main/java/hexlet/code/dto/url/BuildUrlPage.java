package hexlet.code.dto.url;

import hexlet.code.dto.BasePage;
import io.javalin.validation.ValidationError;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildUrlPage extends BasePage {
    private String url;
    private Map<String, List<ValidationError<Object>>> errors;
}
