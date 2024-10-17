package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class AppTest {

    private static Javalin app;

    @BeforeEach
    public final void beforeEach() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterEach
    public final void afterEach() {
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

    @Test
    public void testSearshUrl() throws SQLException {
        var url = new Url("http://localhost:7070");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://localhost:7070");
        });
    }

}
