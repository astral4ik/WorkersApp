package itmo.lab.storage;

/**
 * Исключение при ошибках доступа к данным.
 */
public class DataAccessException extends Exception {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}