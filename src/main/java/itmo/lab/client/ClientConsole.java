package itmo.lab.client;

import itmo.lab.data.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Консоль для ввода и вывода данных клиента.
 */
public class ClientConsole {

    private InputSource inputSource;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ClientEditors editors;
    private ClientInputs inputs;
    private boolean fromFile = false;

    /**
     * Конструктор консоли.
     */
    public ClientConsole() {
        this.inputSource = new ConsoleInputSource();
        this.editors = new ClientEditors();
        this.inputs = new ClientInputs();
    }

    /**
     * Переключить источник ввода.
     * @param newInputSource новый источник ввода
     */
    public void setInputSource(InputSource newInputSource) {
        this.inputSource = newInputSource;
    }

    /**
     * Получить текущий источник ввода.
     * @return источник ввода
     */
    public InputSource getInputSource() {
        return inputSource;
    }

    /**
     * Проверить режим чтения из файла.
     * @return true если читается из файла
     */
    public boolean isFromFile() {
        return fromFile;
    }

    /**
     * Установить режим чтения из файла.
     * @param fromFile режим
     */
    public void setFromFile(boolean fromFile) {
        this.fromFile = fromFile;
    }

    /**
     * Получить объект ввода.
     * @return объект ClientInputs
     */
    public ClientInputs getInputs() {
        return inputs;
    }

    /**
     * Вывод строки с переводом строки.
     * @param message сообщение для вывода
     */
    public void printLine(String message) {
        System.out.println(message);
    }

    /**
     * Вывод строки без перевода строки.
     * @param message сообщение для вывода
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Вывод сообщения.
     * @param message сообщение для вывода
     */
    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Запрос строки у пользователя.
     * @param prompt приглашение
     * @return введенная строка
     */
    public String ask(String prompt) {
        print(prompt);
        String line = inputSource.readLine();
        return (line != null) ? line.trim() : "";
    }

    /**
     * Запрос целого числа.
     * @param prompt приглашение
     * @return введенное число
     */
    public int askInt(String prompt) {
        while (true) {
            try {
                print(prompt);
                String line = inputSource.readLine();
                if (line == null) return 0;
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                printLine("Некорректное число. Попробуйте снова.");
            }
        }
    }

    /**
     * Запрос дробного числа.
     * @param prompt приглашение
     * @return введенное число
     */
    public float askFloat(String prompt) {
        while (true) {
            try {
                print(prompt);
                String line = inputSource.readLine();
                if (line == null) return 0;
                return Float.parseFloat(line.trim());
            } catch (NumberFormatException e) {
                printLine("Некорректное число. Попробуйте снова.");
            }
        }
    }

    /**
     * Запрос длинного целого числа.
     * @param prompt приглашение
     * @return введенное число
     */
    public long askLong(String prompt) {
        while (true) {
            try {
                print(prompt);
                String line = inputSource.readLine();
                if (line == null) return 0;
                return Long.parseLong(line.trim());
            } catch (NumberFormatException e) {
                printLine("Некорректное число. Попробуйте снова.");
            }
        }
    }

    /**
     * Запрос даты и времени.
     * @param prompt приглашение
     * @return введенная дата и время
     */
    public LocalDateTime askLocalDateTime(String prompt) {
        while (true) {
            try {
                print(prompt + " (формат: yyyy-MM-dd HH:mm): ");
                String line = inputSource.readLine();
                if (line == null) return LocalDateTime.now();
                return LocalDateTime.parse(line.trim(), DATE_FORMATTER);
            } catch (Exception e) {
                printLine("Неверный формат даты. Пример: 2024-01-20 08:00");
            }
        }
    }

    /**
     * Запрос строки с валидацией.
     * @param prompt приглашение
     * @param errorMessage сообщение об ошибке
     * @return валидная строка
     */
    public String askValidString(String prompt, String errorMessage) {
        while (true) {
            String input = ask(prompt).trim();
            if (input.isEmpty()) {
                printLine(errorMessage);
                continue;
            }
            if (inputs.containsInvalidChars(input)) {
                printLine("Строка содержит запрещённые символы: < > & ' \"");
                continue;
            }
            return input;
        }
    }

    /**
     * Запрос непустой строки.
     * @param prompt приглашение
     * @param errorMessage сообщение об ошибке
     * @return непустая строка
     */
    public String askNonEmptyString(String prompt, String errorMessage) {
        while (true) {
            String input = ask(prompt).trim();
            if (!input.isEmpty()) {
                return input;
            }
            printLine(errorMessage);
        }
    }

    /**
     * Ввод данных работника.
     * @return работник
     */
    public Worker inputWorker() {
        printLine("\n═══════════════════════════════════");
        printLine("       СОЗДАНИЕ НОВОГО РАБОТНИКА");
        printLine("═══════════════════════════════════");

        String name = askValidString("Введите имя: ", "Имя не может быть пустым!");

        printLine("Введите координаты:");
        int x;
        while (true) {
            x = askInt("  X (макс. 717): ");
            if (x <= 717) break;
            printLine("X не может превышать 717!");
        }
        float y = askFloat("  Y: ");
        Coordinates coordinates = new Coordinates(x, y);

        int salary;
        while (true) {
            salary = askInt("Введите зарплату (больше 0): ");
            if (salary > 0) break;
            printLine("Зарплата должна быть больше 0!");
        }

        LocalDateTime startDate = askLocalDateTime("Введите дату начала работы");

        Position position = inputEnum(Position.class, "Введите должность (MANAGER, LABORER, BAKER): ");

        Status status = inputNullableEnum(Status.class, "Введите статус (или Enter для пропуска): ");

        Organization organization = inputNullableOrganization();

        Worker worker = new Worker(0, name, coordinates, salary, startDate, position, status, organization);
        return worker;
    }

    /**
     * Редактирование работника.
     * @param existing работник для редактирования
     * @return отредактированный работник
     */
    public Worker editWorker(Worker existing) {
        printLine("\n═══════════════════════════════════");
        printLine("       РЕДАКТИРОВАНИЕ РАБОТНИКА");
        printLine("═══════════════════════════════════");

        String name = editors.editString("Имя", existing.getName());
        existing.setName(name);

        Coordinates coords = existing.getCoordinates();
        printLine("Координаты:");
        int x = editors.editInt("X (макс. 717)", coords.getX(), v -> (v <= 717) ? 0 : 1);
        float y = editors.editFloat("Y", coords.getY());
        existing.setCoordinates(new Coordinates(x, y));

        int salary = editors.editInt("Зарплата (> 0)", existing.getSalary(), v -> (v > 0) ? 0 : 1);
        existing.setSalary(salary);

        LocalDateTime startDate = editors.editLocalDateTime("Дата начала", existing.getStartDate());
        existing.setStartDate(startDate);

        Position position = editors.editEnum(Position.class, existing.getPosition());
        existing.setPosition(position);

        Status status = editors.editNullableEnum(Status.class, existing.getStatus());
        existing.setStatus(status);

        return existing;
    }

    private <T extends Enum<T>> T inputEnum(Class<T> enumClass, String prompt) {
        printLine("Доступные значения:");
        for (T value : enumClass.getEnumConstants()) {
            printLine("  - " + value.name());
        }
        while (true) {
            try {
                String input = ask(prompt).trim().toUpperCase();
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                printLine("Неверное значение! Выберите из списка.");
            }
        }
    }

    private <T extends Enum<T>> T inputNullableEnum(Class<T> enumClass, String prompt) {
        printLine("Доступные значения:");
        for (T value : enumClass.getEnumConstants()) {
            printLine("  - " + value.name());
        }
        printLine("  (нажмите Enter для пропуска)");
        while (true) {
            String input = ask(prompt).trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                printLine("Неверное значение! Выберите из списка.");
            }
        }
    }

    private Organization inputNullableOrganization() {
        printLine("Введите организацию:");

        String choice = ask("  Добавить организацию? (y/n): ").trim().toLowerCase();
        if (!choice.equals("y") && !choice.equals("yes")) {
            printLine("  Организация пропущена.");
            return null;
        }

        String fullName = ask("  Название: ").trim();
        if (fullName.isEmpty()) {
            printLine("  Название не указано, организация пропущена.");
            return null;
        }

        Integer annualTurnover = null;
        String turnoverStr = ask("  Годовой оборот (или Enter для пропуска): ").trim();
        if (!turnoverStr.isEmpty()) {
            try {
                annualTurnover = Integer.parseInt(turnoverStr);
            } catch (NumberFormatException e) {
                printLine("  Некорректное число, оборот пропущен.");
            }
        }

        int employeesCount = 0;
        while (employeesCount <= 0) {
            String countStr = ask("  Количество сотрудников: ").trim();
            try {
                employeesCount = Integer.parseInt(countStr);
                if (employeesCount <= 0) {
                    printLine("  Количество должно быть больше 0!");
                }
            } catch (NumberFormatException e) {
                printLine("  Некорректное число!");
            }
        }

        Address address = inputNullableAddress();

        return new Organization(fullName, annualTurnover, address, employeesCount);
    }

    private Address inputNullableAddress() {
        String choice = ask("  Добавить адрес? (y/n): ").trim().toLowerCase();
        if (!choice.equals("y") && !choice.equals("yes")) {
            return null;
        }

        String street = ask("  Улица: ").trim();
        if (street.isEmpty()) {
            printLine("  Улица не указана, адрес пропущен.");
            return null;
        }

        Location location = inputNullableLocation();

        return new Address(street, location);
    }

    private Location inputNullableLocation() {
        String choice = ask("  Добавить город? (y/n): ").trim().toLowerCase();
        if (!choice.equals("y") && !choice.equals("yes")) {
            printLine("  Город пропущен.");
            return null;
        }
        return inputLocation();
    }

    private Location inputLocation() {
        long x = askLong("  X: ");
        int y = askInt("  Y: ");
        int z = askInt("  Z: ");
        return new Location(x, y, z);
    }

    /**
     * Вывод одного работника.
     * @param w работник для вывода
     */
    public void printWorker(Worker w) {
        printLine("  ╔══════════════════════════════════╗");
        printLine("  ║ Работник #" + w.getId() + "                   ║");
        printLine("  ╠══════════════════════════════════╣");
        printLine("  ║ Имя: " + w.getName());
        printLine("  ║ Должность: " + w.getPosition());
        printLine("  ║ Зарплата: " + w.getSalary() + " руб.");
        if (w.getCreationDate() != null) {
            printLine("  ║ Дата создания: " + w.getCreationDate().format(DATE_FORMATTER));
        }
        printLine("  ║ Дата приёма: " + w.getStartDate().format(DATE_FORMATTER));
        if (w.getStatus() != null) {
            printLine("  ║ Статус: " + w.getStatus());
        }
        if (w.getOrganization() != null && w.getOrganization().getFullName() != null) {
            printLine("  ║ Организация: " + w.getOrganization().getFullName());
        }
        printLine("  ╚══════════════════════════════════╝");
    }

    /**
     * Вывод списка работников.
     * @param workers список работников
     */
    public void printWorkersList(List<Worker> workers) {
        if (workers == null || workers.isEmpty()) {
            printLine("Список сотрудников пуст.");
            return;
        }

        printLine("═══════════════════════════════════");
        printLine("     СПИСОК РАБОТНИКОВ (" + workers.size() + ")");
        printLine("═══════════════════════════════════");

        for (Worker w : workers) {
            printWorker(w);
            printLine("");
        }

        printLine("═══════════════════════════════════");
    }

    /**
     * Закрытие сканера.
     */
    public void close() {
        // no-op now
    }
}