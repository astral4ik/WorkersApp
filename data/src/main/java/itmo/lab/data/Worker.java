package itmo.lab.data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Работник предприятия с полным набором характеристик.
 */
public class Worker implements Comparable<Worker>, Serializable {
    private int id;
    private String name;
    private Coordinates coordinates;

    private LocalDateTime creationDate;

    private int salary;

    private LocalDateTime startDate;

    private Position position;
    private Status status;
    private Organization organization;
    private String ownerLogin;

    public Worker() {
    }

    /**
     * Создаёт работника с полным набором обязательных полей.
     *
     * @param id           уникальный идентификатор
     * @param name         имя работника (не пустое)
     * @param coordinates  координаты (не null)
     * @param salary       зарплата (больше 0)
     * @param startDate    дата начала работы (не null)
     * @param position     должность (не null)
     * @param status       статус (может быть null)
     * @param organization организация (может быть null)
     */
    public Worker(int id, String name, Coordinates coordinates, int salary,
                  LocalDateTime startDate, Position position, Status status, Organization organization) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Дата начала работы не может быть null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Должность не может быть null");
        }

        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.organization = organization;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }

    /**
     * Устанавливает имя работника.
     *
     * @param name имя (не может быть пустым или null)
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public int getSalary() { return salary; }

    /**
     * Устанавливает зарплату работника.
     *
     * @param salary зарплата (должна быть больше 0)
     */
    public void setSalary(int salary) {
        if (salary <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
        this.salary = salary;
    }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getOwnerLogin() { return ownerLogin; }
    public void setOwnerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; }

    /**
     * Сравнивает работников по идентификатору.
     */
    @Override
    public int compareTo(Worker o) { return Integer.compare(this.id, o.id); }

    @Override
    public String toString() {
        return "Worker{id=" + id + ", name='" + name + "', salary=" + salary + ", position=" + position + "}";
    }
}
