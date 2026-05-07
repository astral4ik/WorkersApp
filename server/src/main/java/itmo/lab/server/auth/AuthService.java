package itmo.lab.server.auth;

import itmo.lab.server.db.UserRepository;

/**
 * Сервис аутентификации и регистрации пользователей.
 */
public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * Проверяет соответствие Login и пароля записи в БД.
     *
     * @param login       Login пользователя
     * @param rawPassword пароль в открытом виде
     * @return {@code true}, если аутентификация прошла успешно
     */
    public boolean authenticate(String login, String rawPassword) {
        if (login == null || rawPassword == null) return false;
        String hashed = PasswordUtils.hash(rawPassword);
        return userRepository.authenticate(login, hashed);
    }

    /**
     * Регистрирует нового пользователя с хешированным паролем.
     *
     * @param login       Login нового пользователя
     * @param rawPassword пароль в открытом виде
     * @return {@code true}, если регистрация прошла успешно
     */
    public boolean register(String login, String rawPassword) {
        if (login == null || login.isBlank()) return false;
        if (rawPassword == null || rawPassword.isBlank()) return false;
        String hashed = PasswordUtils.hash(rawPassword);
        return userRepository.register(login, hashed);
    }
}
