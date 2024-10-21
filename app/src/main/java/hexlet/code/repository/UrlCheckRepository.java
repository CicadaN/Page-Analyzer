package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO URL_CHECKS (STATUS_CODE, TITLE, H1, DESCRIPTION, URL_ID, CREATED_AT) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, urlCheck.getStatusCode());
            stmt.setString(2, urlCheck.getTitle());
            stmt.setString(3, urlCheck.getH1());
            stmt.setString(4, urlCheck.getDescription());
            stmt.setInt(5, urlCheck.getUrlId());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();

            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                urlCheck.setId(rs.getInt(1));
            } else {
                throw new SQLException("Failed to save URL check");
            }
        }
    }

    public static Map<Integer, UrlCheck> findLatestCheckUrl()  throws SQLException {
        String sql = "SELECT DISTINCT ON (url_id) * "
            + "from url_checks order by url_id DESC, id DESC";
        try (var connection = dataSource.getConnection();
            var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            HashMap<Integer, UrlCheck> result = new HashMap<Integer, UrlCheck>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int urlId = resultSet.getInt("URL_ID");
                int statusCode = resultSet.getInt("STATUS_CODE");
                String title = resultSet.getString("TITLE");
                String h1 = resultSet.getString("H1");
                String description = resultSet.getString("DESCRIPTION");
                Timestamp timestamp = resultSet.getTimestamp("CREATED_AT");
                UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description);
                urlCheck.setId(id);
                urlCheck.setUrlId(urlId);
                urlCheck.setCreatedAt(timestamp);
                result.put(urlId, urlCheck);
            }
            return result;
        }
    }

    public static List<UrlCheck> findbyId(int urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE URL_ID = ? ORDER BY id DESC";
        List<UrlCheck> result = new ArrayList<>();

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, urlId);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int statusCode = resultSet.getInt("STATUS_CODE");
                String title = resultSet.getString("TITLE");
                String h1 = resultSet.getString("H1");
                String description = resultSet.getString("DESCRIPTION");
                Timestamp timestamp = resultSet.getTimestamp("CREATED_AT");
                UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description);
                urlCheck.setId(id);
                urlCheck.setUrlId(urlId);
                urlCheck.setCreatedAt(timestamp);
                result.add(urlCheck);
            }
        }
        return result;
    }
}
