package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

public class AppTest {

    private static Javalin app;
    private static String baseUrl;

    @BeforeEach
    public void beforeAll() throws IOException, SQLException {
        app = App.getApp();
        baseUrl = "http://localhost:7070";
    }

    @AfterEach
    public void afterAll() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Welcome to Page Analyzer");
        });
    }
    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testJsonPage() throws SQLException {
        var url = new Url("https://yandex.ru");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testNotFoundUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/666");
            assertThat(response.code()).isEqualTo(404);
        });
    }

}
