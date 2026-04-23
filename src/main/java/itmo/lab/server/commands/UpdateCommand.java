package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда обновления данных работника по ID.
 */
public class UpdateCommand implements ServerCommand {

    private final WorkersCollection collection;

    public UpdateCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        Object[] arr = (Object[]) args;
        int id = ((IdArgument) arr[0]).getId();
        Worker worker = ((WorkerArgument) arr[1]).getWorker();

        if (collection.update(id, worker)) {
            return new Response(true, "Работник обновлен", null);
        }
        return new Response(false, "Работник с ID " + id + " не найден", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}