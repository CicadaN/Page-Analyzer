package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    public static HikariDataSource getDataSource() {
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

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        return new HikariDataSource(config);
    }
}