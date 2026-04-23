package itmo.lab.common;

import java.io.Serializable;

/**
 * Аргумент команды - строковое значение.
 */
public class StringArgument implements Serializable {
    private String value;
    public StringArgument(String value) { this.value = value; }
    public String getValue() { return value; }
}