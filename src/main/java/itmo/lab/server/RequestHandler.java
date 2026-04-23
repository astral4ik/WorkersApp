package itmo.lab;

import itmo.lab.server.commands.ServerCommand;
import itmo.lab.server.commands.*;
import itmo.lab.storage.CollectionStorage;
import itmo.lab.storage.DataAccessException;
import itmo.lab.common.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик запросов от клиентов.
 *
 * Маппит имена команд на объекты Command и выполняет их.
 * После выполнения модифицирующих команд автоматически сохраняет коллекцию.
 */
public class RequestHandler {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RequestHandler.class);

    private final Map<String, ServerCommand> commands;
    private final WorkersCollection collection;
    private final CollectionStorage storage;

    public RequestHandler(WorkersCollection collection, CollectionStorage storage) {
        this.collection = collection;
        this.storage = storage;
        
        commands = new HashMap<>();
        commands.put("insert", new InsertCommand(collection));
        commands.put("update", new UpdateCommand(collection));
        commands.put("get", new GetWorkerCommand(collection));
        commands.put("remove_key", new RemoveKeyCommand(collection));
        commands.put("remove_lower", new RemoveLowerCommand(collection));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(collection));
        commands.put("clear", new ClearCommand(collection));
        commands.put("show", new ShowCommand(collection));
        commands.put("info", new InfoCommand(collection));
        commands.put("filter_contains_name", new FilterContainsNameCommand(collection));
        commands.put("count_less_than_start_date", new CountLessThanStartDateCommand(collection));
        commands.put("print_unique_start_date", new PrintUniqueStartDateCommand(collection));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(collection));
        commands.put("save", new SaveCommand(collection, storage));
    }

    /**
     * Обработать запрос.
     */
    public Response handle(itmo.lab.common.Request request) {
        logger.info("Обработка команды: " + request.getCommandName());

        ServerCommand command = commands.get(request.getCommandName());

        if (command == null) {
            logger.error("Команда не найдена: " + request.getCommandName());
            return new Response(false, "Неизвестная команда: " + request.getCommandName(), null);
        }

        try {
            logger.info("Выполнение команды: " + request.getCommandName());
            Response response = command.execute(request.getArgs());
            logger.info("Команда выполнена, успех: " + response.isSuccess());

            if (command.isModifying() && response.isSuccess()) {
                try {
                    storage.save(collection);
                    logger.info("Коллекция сохранена в файл");
                } catch (DataAccessException e) {
                    logger.error("Ошибка сохранения: " + e.getMessage());
                    return new Response(false, "Ошибка сохранения: " + e.getMessage(), null);
                }
            }

            return response;
        } catch (Exception e) {
            logger.error("Ошибка выполнения команды " + request.getCommandName() + ": " + e.getMessage());
            e.printStackTrace();
            return new Response(false, "Ошибка выполнения: " + e.getMessage(), null);
        }
    }
}