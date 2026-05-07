package itmo.lab.data;

import java.io.Serializable;

/**
 * Адрес организации с названием улицы и координатами города.
 */
public class Address implements Serializable {
    private String street;
    private Location town;

    public Address() {
    }

    /**
     * Создаёт адрес по улице и местоположению города.
     *
     * @param street название улицы
     * @param town   координаты города
     */
    /**
     * Создаёт адрес с проверкой обязательного поля улицы.
     *
     * @param street название улицы (не может быть null)
     * @param town   координаты города (может быть null)
     */
    public Address(String street, Location town) {
        if (street == null) {
            throw new IllegalArgumentException("Улица не может быть null");
        }
        this.street = street;
        this.town = town;
    }

    public String getStreet() { return street; }

    /**
     * Устанавливает название улицы.
     *
     * @param street значение (не может быть null)
     */
    public void setStreet(String street) {
        if (street == null) {
            throw new IllegalArgumentException("Улица не может быть null");
        }
        this.street = street;
    }

    public Location getTown() { return town; }
    public void setTown(Location town) { this.town = town; }
}
