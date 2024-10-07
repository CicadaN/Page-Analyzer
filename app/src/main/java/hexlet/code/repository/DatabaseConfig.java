package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {

    // Создаем логгер
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    public static HikariDataSource getDataSource() {
        // Получаем переменные окружения
        String host = System.getenv("HOST");
        String port = System.getenv("DB_PORT");
        String database = System.getenv("DATABASE");
        String user = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        // Логируем переменные окружения
        logger.info("Database connection parameters:");
        logger.info("Host: {}", host);
        logger.info("Port: {}", port);
        logger.info("Database: {}", database);
        logger.info("User: {}", user);
        // Логируем факт получения переменной password, но само значение лучше не выводить
        if (password != null) {
            logger.info("Password: [HIDDEN]");
        } else {
            logger.warn("Password is not set!");
        }

        // Формируем строку подключения
        String jdbcUrl = String.format(
                "jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
                host, port, database, user, password
        );

        // Логируем строку подключения (без пароля для безопасности)
        logger.info("JDBC URL: jdbc:postgresql://{}:{}/{}?user={}&password=[HIDDEN]", host, port, database, user);

        // Логируем начало создания HikariCP DataSource
        logger.info("Creating HikariCP DataSource");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        try {
            // Логируем успешное создание DataSource
            HikariDataSource dataSource = new HikariDataSource(config);
            logger.info("HikariCP DataSource created successfully");
            return dataSource;
        } catch (Exception e) {
            // Логируем ошибку при создании DataSource
            logger.error("Failed to create HikariCP DataSource", e);
            throw new RuntimeException(e);
        }
    }
}

