package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;

public abstract class BaseRepository {
    protected HikariDataSource dataSource;

    public BaseRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }
}

