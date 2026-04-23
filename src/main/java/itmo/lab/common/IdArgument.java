package itmo.lab.common;

import java.io.Serializable;

/**
 * Аргумент команды - идентификатор работника.
 */
public class IdArgument implements Serializable {
    private int id;
    public IdArgument(int id) { this.id = id; }
    public int getId() { return id; }
}