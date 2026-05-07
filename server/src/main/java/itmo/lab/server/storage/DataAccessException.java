package itmo.lab.server.storage;

/**
 * Непроверяемое исключение, сигнализирующее об ошибке доступа к данным в DB.
 */
public class DataAccessException extends RuntimeException {

    /**
     * @param message описание ошибки
     * @param cause   исходная причина
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message описание ошибки
     */
    public DataAccessException(String message) {
        super(message);
    }
}
