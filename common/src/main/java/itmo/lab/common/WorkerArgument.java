package itmo.lab.common;

import java.io.Serializable;

/**
 * Аргумент команды, содержащий объект Worker (хранится как Object во избежание циклической зависимости модулей).
 */
public class WorkerArgument implements Serializable {
    private Object worker;

    /**
     * @param worker объект Worker, передаваемый как аргумент команды
     */
    public WorkerArgument(Object worker) { this.worker = worker; }

    public Object getWorker() { return worker; }
}
