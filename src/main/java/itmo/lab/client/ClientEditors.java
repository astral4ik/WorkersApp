package itmo.lab.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.IntUnaryOperator;

/**
 * Утилиты для редактирования данных клиентом.
 */
public class ClientEditors {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ClientConsole console;

    /**
     * Конструктор.
     * @param console консоль для ввода
     */
    public ClientEditors(ClientConsole console) {
        this.console = console;
    }

    /**
     * Конструктор без параметров.
     */
    public ClientEditors() {
    }

    /**
     * Редактирование строки.
     * @param fieldName название поля
     * @param current текущее значение
     * @return новое значение или текущее
     */
    public String editString(String fieldName, String current) {
        printPrompt(fieldName, current);
        String input = console.getInputSource().readLine();
        if (input == null) return current;
        input = input.trim();
        return input.isEmpty() ? current : input;
    }

    /**
     * Редактирование целого числа.
     * @param fieldName название поля
     * @param current текущее значение
     * @param validator валидатор
     * @return новое значение или текущее
     */
    public Integer editInt(String fieldName, Integer current, IntUnaryOperator validator) {
        while (true) {
            printPrompt(fieldName, String.valueOf(current));
            String input = console.getInputSource().readLine();
            if (input == null) return current;
            input = input.trim();
            if (input.isEmpty()) {
                return current;
            }
            try {
                int value = Integer.parseInt(input);
                if (validator.applyAsInt(value) == 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * Редактирование дробного числа.
     * @param fieldName название поля
     * @param current текущее значение
     * @return новое значение или текущее
     */
    public Float editFloat(String fieldName, Float current) {
        while (true) {
            printPrompt(fieldName, String.valueOf(current));
            String input = console.getInputSource().readLine();
            if (input == null) return current;
            input = input.trim();
            if (input.isEmpty()) {
                return current;
            }
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * Редактирование даты и времени.
     * @param fieldName название поля
     * @param current текущее значение
     * @return новое значение или текущее
     */
    public LocalDateTime editLocalDateTime(String fieldName, LocalDateTime current) {
        while (true) {
            printPrompt(fieldName, current.format(DATE_FORMATTER));
            String input = console.getInputSource().readLine();
            if (input == null) return current;
            input = input.trim();
            if (input.isEmpty()) {
                return current;
            }
            try {
                return LocalDateTime.parse(input, DATE_FORMATTER);
            } catch (Exception e) {
                printMessage("Неверный формат. Используйте: yyyy-MM-dd HH:mm");
            }
        }
    }

    /**
     * Редактирование enum.
     * @param enumClass класс enum
     * @param current текущее значение
     * @return новое значение или текущее
     */
    public <T extends Enum<T>> T editEnum(Class<T> enumClass, T current) {
        while (true) {
            printEnumOptions(enumClass, current);
            printMessage(" (или Enter - оставить текущее)");
            String input = console.getInputSource().readLine();
            if (input == null) return current;
            input = input.trim();
            if (input.isEmpty()) {
                return current;
            }
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                printMessage("Неверное значение!");
            }
        }
    }

    /**
     * Редактирование nullable enum.
     * @param enumClass класс enum
     * @param current текущее значение
     * @return новое значение, null или текущее
     */
    public <T extends Enum<T>> T editNullableEnum(Class<T> enumClass, T current) {
        while (true) {
            printEnumOptions(enumClass, current);
            printMessage(" (или Enter - оставить текущее)");
            String input = console.getInputSource().readLine();
            if (input == null) return current;
            input = input.trim();
            if (input.isEmpty()) {
                return current;
            }
            if (input.equalsIgnoreCase("null") || input.equalsIgnoreCase("-")) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                printMessage("Неверное значение!");
            }
        }
    }

    private void printPrompt(String fieldName, String current) {
        System.out.print(fieldName + " [" + current + "]: ");
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private <T extends Enum<T>> void printEnumOptions(Class<T> enumClass, T current) {
        System.out.println("Доступные значения:");
        for (T value : enumClass.getEnumConstants()) {
            System.out.println("  - " + value.name());
        }
        if (current != null) {
            System.out.println("Текущее: " + current.name());
        }
    }
}