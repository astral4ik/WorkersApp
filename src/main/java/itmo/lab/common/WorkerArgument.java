package itmo.lab.common;

import itmo.lab.data.Worker;

import java.io.Serializable;

/**
 * Аргумент команды - объект Worker.
 */
public class WorkerArgument implements Serializable {
    private Worker worker;
    public WorkerArgument(Worker worker) { this.worker = worker; }
    public Worker getWorker() { return worker; }
}