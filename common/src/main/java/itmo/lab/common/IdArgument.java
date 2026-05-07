package itmo.lab.common;

import java.io.Serializable;

/**
 * Аргумент команды, содержащий идентификатор работника.
 */
public class IdArgument implements Serializable {
    private int id;

    /**
     * @param id идентификатор работника
     */
    public IdArgument(int id) { this.id = id; }

    public int getId() { return id; }
}
