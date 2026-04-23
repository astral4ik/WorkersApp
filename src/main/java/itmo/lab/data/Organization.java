package itmo.lab.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Организация-работодатель.
 *
 * Содержит название, годовой оборот, количество сотрудников и адрес.
 */
public class Organization implements Serializable {
    private String fullName;
    private Integer annualTurnover;
    
    @JsonProperty("officialAddress")
    private Address address;
    
    private int employeesCount;

    public Organization() {
    }

    public Organization(String fullName, Integer annualTurnover, Address address, int employeesCount) {
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Количество сотрудников должно быть больше 0");
        }
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.address = address;
        this.employeesCount = employeesCount;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Integer getAnnualTurnover() { return annualTurnover; }
    public void setAnnualTurnover(Integer annualTurnover) { this.annualTurnover = annualTurnover; }

    public Address getAddress() { return address; }
    
    public void setAddress(Address address) { this.address = address; }
    
    public int getEmployeesCount() { return employeesCount; }
    public void setEmployeesCount(int employeesCount) {
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Количество сотрудников должно быть больше 0");
        }
        this.employeesCount = employeesCount;
    }
}