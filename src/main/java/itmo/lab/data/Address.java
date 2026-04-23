package itmo.lab.data;

import java.io.Serializable;

/**
 * Адрес организации.
 *
 * Содержит название улицы и координаты города.
 */
public class Address implements Serializable {
    private String street;
    private Location town;

    /**
     * Конструктор по умолчанию.
     */
    public Address() {
    }

    /**
     * Конструктор с параметрами.
     * @param street название улицы
     * @param town координаты города
     */
    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }

    /**
     * Получить название улицы.
     * @return название улицы
     */
    public String getStreet() { return street; }

    /**
     * Установить название улицы.
     * @param street название улицы
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Получить координаты города.
     * @return координаты города
     */
    public Location getTown() { return town; }

    /**
     * Установить координаты города.
     * @param town координаты города
     */
    public void setTown(Location town) { this.town = town; }
}