package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class AppTest {

    private static Javalin app;
    private static MockWebServer server;

    private static String readResourceFile(String fileName) throws IOException {
        try (InputStream inputStream = AppTest.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        }
    }

    @BeforeAll
    public static void startMock() throws IOException {
        server = new MockWebServer();
        MockResponse response = new MockResponse()
                .setBody(readResourceFile("MockWebServer.html")).setResponseCode(200);
        server.enqueue(response);
        server.start();
    }

    @AfterAll
    public static void stopMockServer() throws IOException {
        server.shutdown();
    }

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
        var url = new Url("https://ya.ru");
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

    @Test
    public void testMock() {
        String mockServerUrl = server.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            Url url = new Url(mockServerUrl);
            UrlRepository.save(url);

            client.post(NamedRoutes.urlCheck(url.getId()));
            var checkUrl = UrlCheckRepository.findbyId(url.getId());
            var title = checkUrl.getFirst().getTitle();
            var h1 = checkUrl.getFirst().getH1();
            assertThat(title).isEqualTo("MockWebServer");
            assertThat(h1).isEqualTo("Hello World Server !");
        });
    }
}
