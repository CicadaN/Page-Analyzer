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
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO URLS (NAME, CREATED_AT) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            Instant instant = Instant.now();
            url.setCreatedAt(instant);
            stmt.setTimestamp(2, Timestamp.from(instant));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                url.setId(rs.getInt(1));
            } else {
                throw new SQLException("Failed to save url");
            }
        }
    }

    public static Optional<Url> findById(int id) throws SQLException {
        String sql = "SELECT * FROM URLS WHERE ID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUrl(rs));
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM URLS WHERE NAME = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUrl(rs));
            }
            return Optional.empty();
        }
    }

    public static List<Url> findAll() throws SQLException {
        String sql = "SELECT * FROM urls";
        List<Url> urls = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                urls.add(mapResultSetToUrl(rs));
            }
        }
        return urls;
    }

    private static Url mapResultSetToUrl(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        Timestamp timestamp = rs.getTimestamp("CREATED_AT");
        Url url = new Url(name);
        url.setId(id);
        url.setCreatedAt(timestamp.toInstant());
        return url;
    }
}
