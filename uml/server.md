classDiagram
    class Request {
        <<Serializable>>
        -String commandName
        -Serializable args
        -String login
        -String password
        +getCommandName() String
        +getArgs() Serializable
        +getLogin() String
        +getPassword() String
    }

    class Response {
        <<Serializable>>
        -boolean success
        -String message
        -Object data
        +isSuccess() boolean
        +getMessage() String
        +getData() Object
    }

    class IdArgument {
        <<Serializable>>
        -int id
        +getId() int
    }

    class WorkerArgument {
        <<Serializable>>
        -Object worker
        +getWorker() Object
    }

    class StringArgument {
        <<Serializable>>
        -String value
        +getValue() String
    }

    class Server {
        -int PORT
        -WorkersCollection collection
        -RequestHandler requestHandler
        -ExecutorService readPool
        -ExecutorService processPool
        -ExecutorService sendPool
        +start()
        +main(String[])
    }

    class WorkersCollection {
        -TreeMap workers
        -LocalDateTime creationTime
        -ReadWriteLock lock
        +loadFromDatabase()
        +insert(Worker, String) long
        +update(Worker, String) boolean
        +remove(int, String) boolean
        +clear(String) int
        +getAll() List
        +get(int) Worker
        +isOwner(int, String) boolean
        +removeLower(int, String) int
        +removeGreaterKey(int, String) int
        +getUniqueStartDates() List
        +size() int
    }

    class RequestHandler {
        -Map commands
        -AuthService authService
        +handle(Request) Response
    }

    class AuthService {
        +authenticate(String, String) boolean
        +register(String, String) boolean
    }

    class PasswordUtils {
        +hash(String) String
        +verify(String, String) boolean
    }

    class DatabaseManager {
        +init(String, String)
        +getConnection() Connection
    }

    class UserRepository {
        +register(String, String) boolean
        +authenticate(String, String) boolean
    }

    class WorkerRepository {
        +loadAll() Map
        +insert(Worker, String) long
        +update(Worker, String) boolean
        +delete(long, String) boolean
        +deleteByOwner(String) int
    }

    class OrganizationRepository {
        +insert(Organization) long
        +update(long, Organization)
        +delete(long)
        +findById(long) Organization
    }

    class DataAccessException {
        <<RuntimeException>>
    }

    class ServerCommand {
        <<interface>>
        +execute(Serializable, String) Response
        +isModifying() boolean
    }

    class InsertCommand { +execute(Serializable, String) Response }
    class UpdateCommand { +execute(Serializable, String) Response }
    class RemoveKeyCommand { +execute(Serializable, String) Response }
    class RemoveLowerCommand { +execute(Serializable, String) Response }
    class RemoveGreaterKeyCommand { +execute(Serializable, String) Response }
    class ClearCommand { +execute(Serializable, String) Response }
    class ShowCommand { +execute(Serializable, String) Response }
    class InfoCommand { +execute(Serializable, String) Response }
    class GetWorkerCommand { +execute(Serializable, String) Response }
    class FilterContainsNameCommand { +execute(Serializable, String) Response }
    class CountLessThanStartDateCommand { +execute(Serializable, String) Response }
    class PrintUniqueStartDateCommand { +execute(Serializable, String) Response }
    class ReplaceIfLowerCommand { +execute(Serializable, String) Response }

    InsertCommand ..|> ServerCommand
    UpdateCommand ..|> ServerCommand
    RemoveKeyCommand ..|> ServerCommand
    RemoveLowerCommand ..|> ServerCommand
    RemoveGreaterKeyCommand ..|> ServerCommand
    ClearCommand ..|> ServerCommand
    ShowCommand ..|> ServerCommand
    InfoCommand ..|> ServerCommand
    GetWorkerCommand ..|> ServerCommand
    FilterContainsNameCommand ..|> ServerCommand
    CountLessThanStartDateCommand ..|> ServerCommand
    PrintUniqueStartDateCommand ..|> ServerCommand
    ReplaceIfLowerCommand ..|> ServerCommand

    Server *-- WorkersCollection
    Server *-- RequestHandler
    RequestHandler --> WorkersCollection
    RequestHandler *-- AuthService
    RequestHandler o-- ServerCommand
    RequestHandler ..> Request : принимает
    RequestHandler ..> Response : возвращает
    AuthService *-- UserRepository
    AuthService ..> PasswordUtils
    UserRepository ..> DatabaseManager
    WorkersCollection *-- WorkerRepository
    WorkerRepository *-- OrganizationRepository
    WorkerRepository ..> DatabaseManager
    OrganizationRepository ..> DatabaseManager
    WorkerRepository ..> DataAccessException
    OrganizationRepository ..> DataAccessException
    UserRepository ..> DataAccessException

    InsertCommand ..> WorkerArgument
    UpdateCommand ..> IdArgument
    UpdateCommand ..> WorkerArgument
    RemoveKeyCommand ..> IdArgument
    RemoveLowerCommand ..> IdArgument
    RemoveGreaterKeyCommand ..> IdArgument
    GetWorkerCommand ..> IdArgument
    ReplaceIfLowerCommand ..> IdArgument
    ReplaceIfLowerCommand ..> WorkerArgument
    FilterContainsNameCommand ..> StringArgument
    CountLessThanStartDateCommand ..> StringArgument
