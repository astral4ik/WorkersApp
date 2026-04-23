package itmo.lab.client;

/**
 * Утилиты для ввода данных клиентом.
 */
public class ClientInputs {

    private static final String INVALID_CHARS = "<>&'\"";

    /**
     * Проверка строки на запрещённые символы.
     * @param input строка для проверки
     * @return true если содержит запрещённые символы
     */
    public boolean containsInvalidChars(String input) {
        for (char c : INVALID_CHARS.toCharArray()) {
            if (input.indexOf(c) >= 0) return true;
        }
        return false;
    }
}