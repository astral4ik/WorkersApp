package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда замены если зарплата ниже.
 */
public class ReplaceIfLowerCommand implements ServerCommand {

    private final WorkersCollection collection;

    public ReplaceIfLowerCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        Object[] arr = (Object[]) args;
        int id = ((IdArgument) arr[0]).getId();
        Worker newWorker = ((WorkerArgument) arr[1]).getWorker();

        Worker existing = collection.get(id);
        if (existing == null) {
            return new Response(false, "Работник с ID " + id + " не найден", null);
        }

        if (newWorker.getSalary() < existing.getSalary()) {
            newWorker.setId(id);
            collection.update(id, newWorker);
            return new Response(true, "Работник заменен", null);
        } else {
            return new Response(false, "Новая зарплата не ниже текущей", null);
        }
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}