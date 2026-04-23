package itmo.lab.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Работник предприятия.
 *
 * Содержит информацию о сотруднике: ФИО, координаты, зарплата,
 * должность, статус, организация и даты.
 */
public class Worker implements Comparable<Worker>, Serializable {
    private int id;
    private String name;
    private Coordinates coordinates;
    
    @JsonProperty("creationDate")
    private LocalDateTime creationDate;
    
    private int salary;
    
    @JsonProperty("startDate")
    private LocalDateTime startDate;
    
    private Position position;
    private Status status;
    private Organization organization;

    public Worker() {
    }

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
        this.creationDate = (creationDate != null) ? creationDate : LocalDateTime.now();
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.organization = organization;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
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

    @Override
    public int compareTo(Worker o) { return Integer.compare(this.id, o.id); }

    @Override
    public String toString() {
        return "Worker{id=" + id + ", name='" + name + "', salary=" + salary + ", position=" + position + "}";
    }
}