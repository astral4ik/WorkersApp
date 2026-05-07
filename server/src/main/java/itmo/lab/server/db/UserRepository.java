package itmo.lab.server.db;

import itmo.lab.server.storage.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Репозиторий для регистрации и аутентификации пользователей в таблице users.
 */
public class UserRepository {

    /**
     * Регистрирует нового пользователя; ничего не делает, если Login уже занят.
     *
     * @param login          Login пользователя
     * @param hashedPassword хешированный Password
     * @return {@code true}, если пользователь был добавлен
     */
    public boolean register(String login, String hashedPassword) {
        String sql = "INSERT INTO users(login, password) VALUES(?, ?) " +
                     "ON CONFLICT (login) DO NOTHING";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, hashedPassword);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка регистрации пользователя", e);
        }
    }

    /**
     * Проверяет наличие пользователя с указанным Login и хешированным Password в DB.
     *
     * @param login          Login пользователя
     * @param hashedPassword хешированный Password
     * @return {@code true}, если пользователь найден
     */
    public boolean authenticate(String login, String hashedPassword) {
        String sql = "SELECT 1 FROM users WHERE login = ? AND password = ?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, hashedPassword);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка аутентификации пользователя", e);
        }
    }
}
