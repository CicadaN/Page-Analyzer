package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Slf4j
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() throws IOException, SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        String sql = readResourceFile("schema.sql");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        app.get("/", ctx -> ctx.result("Hello World"));
        return app;
    }

    private static String readResourceFile(String fileName) throws IOException {
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static String getDatabaseUrl() {
        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        if (jdbcUrl.contains("${")) {
            String host = System.getenv("HOST");
            String port = System.getenv("DB_PORT");
            String database = System.getenv("DATABASE");
            String username = System.getenv("USERNAME");
            String password = System.getenv("PASSWORD");
            jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
                    host, port, database, username, password);
        }
        logger.info("Using JDBC URL: {}", jdbcUrl.replaceAll("password=\\w+", "password=<masked>"));
        return jdbcUrl;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(getPort());
    }
}