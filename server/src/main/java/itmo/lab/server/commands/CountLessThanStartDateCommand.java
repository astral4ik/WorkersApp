package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Команда подсчёта работников, чья дата начала работы раньше заданной.
 */
public class CountLessThanStartDateCommand implements ServerCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final WorkersCollection collection;

    public CountLessThanStartDateCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        String dateStr = ((StringArgument) args).getValue();
        try {
            LocalDateTime date = LocalDateTime.parse(dateStr, FORMATTER);
            long count = collection.getAll().stream()
                    .filter(w -> w.getStartDate().isBefore(date))
                    .count();
            return new Response(true, "Количество: " + count, null);
        } catch (Exception e) {
            return new Response(false, "Неверный формат даты", null);
        }
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}
