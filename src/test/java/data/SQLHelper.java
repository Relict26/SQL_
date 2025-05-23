package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        String url = System.getProperty("db.url", "jdbc:mysql://localhost:3306/app");
        String user = System.getProperty("db.user", "app");
        String password = System.getProperty("db.password", "pass");

        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            var code = QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<String>());
            return new DataHelper.VerificationCode(code);
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var connection = getConn()) {
            QUERY_RUNNER.execute(connection, "DELETE FROM auth_codes");
            QUERY_RUNNER.execute(connection, "DELETE FROM card_transactions");
            QUERY_RUNNER.execute(connection, "DELETE FROM cards");
            QUERY_RUNNER.execute(connection, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void cleanAuthCodes() {
        try (var connection = getConn()) {
            QUERY_RUNNER.execute(connection, "DELETE FROM auth_codes");
        }
    }
}