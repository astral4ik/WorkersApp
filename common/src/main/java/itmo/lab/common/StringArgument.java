package itmo.lab.common;

import java.io.Serializable;

/**
 * Аргумент команды, содержащий строковое значение.
 */
public class StringArgument implements Serializable {
    private String value;

    /**
     * @param value строковое значение аргумента
     */
    public StringArgument(String value) { this.value = value; }

    public String getValue() { return value; }
}
