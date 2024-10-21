package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO URLS (NAME, CREATED_AT) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            Instant instant = Instant.now();
            url.setCreatedAt(instant);
            stmt.setTimestamp(2, Timestamp.from(instant));
            int affectedRows = stmt.executeUpdate();
            System.out.println("Affected rows: " + affectedRows); // logg

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                url.setId(rs.getInt(1));
                System.out.println("Generated ID: " + url.getId()); // logg
            } else {
                throw new SQLException("Failed to save url");
            }
        }
    }

    public static Url findById(int id) throws SQLException {
        String sql = "SELECT * FROM URLS WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Instant createdAt = rs.getTimestamp("created_at").toInstant();
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
        }
        return null;
    }

    public static Url findByName(String name) throws SQLException {
        String sql = "SELECT * FROM URLS WHERE NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                Instant createdAt = rs.getTimestamp("created_at").toInstant();
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return url;
            }
        }
        return null;
    }

    public static List<Url> findAll() throws SQLException {
        String sql = "SELECT * FROM urls";
        List<Url> urls = new ArrayList<>();
        // Лог для отладки
        System.out.println("Fetching URLs from the database...");
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                Timestamp timestamp = rs.getTimestamp("CREATED_AT");
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(timestamp.toInstant());
                urls.add(url);
            }
        }
        System.out.println("Fetched URLs: " + urls.size()); // Проверка количества полученных URL
        return urls;
    }

}
