package itmo.lab.client;

/**
 * Интерфейс источника ввода.
 */
public interface InputSource {

    /**
     * Прочитать строку из источника ввода.
     * @return прочитанная строка или null, если источник закрыт
     */
    String readLine();
}