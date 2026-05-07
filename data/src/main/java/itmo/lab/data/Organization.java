package itmo.lab.data;

import java.io.Serializable;

/**
 * Организация-работодатель с названием, оборотом и адресом.
 */
public class Organization implements Serializable {
    private String fullName;
    private Integer annualTurnover;
    private Address officialAddress;
    private int employeesCount;

    public Organization() {
    }

    /**
     * Создаёт организацию с проверкой обязательных полей.
     *
     * @param fullName       полное название (может быть null)
     * @param annualTurnover годовой оборот (может быть null, если задан — больше 0)
     * @param officialAddress официальный адрес (не может быть null)
     * @param employeesCount количество сотрудников (больше 0)
     */
    public Organization(String fullName, Integer annualTurnover, Address officialAddress, int employeesCount) {
        if (officialAddress == null) {
            throw new IllegalArgumentException("Адрес организации не может быть null");
        }
        if (annualTurnover != null && annualTurnover <= 0) {
            throw new IllegalArgumentException("Годовой оборот должен быть больше 0");
        }
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Количество сотрудников должно быть больше 0");
        }
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.officialAddress = officialAddress;
        this.employeesCount = employeesCount;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getAnnualTurnover() { return annualTurnover; }

    /**
     * Устанавливает годовой оборот.
     *
     * @param annualTurnover значение больше 0 или null
     */
    public void setAnnualTurnover(Integer annualTurnover) {
        if (annualTurnover != null && annualTurnover <= 0) {
            throw new IllegalArgumentException("Годовой оборот должен быть больше 0");
        }
        this.annualTurnover = annualTurnover;
    }

    public Address getOfficialAddress() { return officialAddress; }

    /**
     * Устанавливает официальный адрес организации.
     *
     * @param officialAddress адрес (не может быть null)
     */
    public void setOfficialAddress(Address officialAddress) {
        if (officialAddress == null) {
            throw new IllegalArgumentException("Адрес организации не может быть null");
        }
        this.officialAddress = officialAddress;
    }

    public int getEmployeesCount() { return employeesCount; }

    /**
     * Устанавливает количество сотрудников.
     *
     * @param employeesCount значение больше 0
     */
    public void setEmployeesCount(int employeesCount) {
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Количество сотрудников должно быть больше 0");
        }
        this.employeesCount = employeesCount;
    }
}
