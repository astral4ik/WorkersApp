package itmo.lab.server.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Утилитный класс для хеширования и проверки паролей с помощью SHA-512.
 */
public class PasswordUtils {

    /**
     * Возвращает SHA-512-хеш переданного пароля в виде шестнадцатеричной строки.
     *
     * @param password пароль в открытом виде
     * @return хеш-строка
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 недоступен", e);
        }
    }

    /**
     * Проверяет соответствие пароля в открытом виде его хешу.
     *
     * @param rawPassword    пароль в открытом виде
     * @param hashedPassword эталонный хеш
     * @return {@code true}, если пароли совпадают
     */
    public static boolean verify(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }
}
