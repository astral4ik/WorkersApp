package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда получения работника по ID.
 */
public class GetWorkerCommand implements ServerCommand {

    private final WorkersCollection collection;

    public GetWorkerCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        int id = ((IdArgument) args).getId();
        Worker worker = collection.get(id);

        if (worker == null) {
            return new Response(false, "Работник с ID " + id + " не найден", null);
        }
        return new Response(true, "Данные получены", worker);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}