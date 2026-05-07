package itmo.lab.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Менеджер JDBC-соединений с базой данных PostgreSQL.
 */
public class DatabaseManager {
    private static String url;
    private static String user;
    private static String password;

    /**
     * Инициализирует параметры подключения к DB.
     *
     * @param dbUser     имя пользователя DB
     * @param dbPassword Password пользователя DB
     */
    public static void init(String dbUser, String dbPassword) {
        user = dbUser;
        password = dbPassword;
        // url = "jdbc:postgresql://localhost:5432/studs";
        url = "jdbc:postgresql://pg:5432/studs";
    }

    /**
     * Открывает и возвращает новое JDBC-соединение с DB.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
