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

    class Client {
        -String SERVER_HOST
        -int SERVER_PORT
        -ClientConsole console
        -CommandManager commandManager
        -String currentLogin
        -String currentPassword
        +connect()
        +run()
        +processCommand(String)
        +sendRequest(Request) Response
        +getCurrentLogin() String
        +getCurrentPassword() String
    }

    class ClientConsole {
        -InputSource inputSource
        -boolean fromFile
        +printLine(String)
        +ask(String) String
        +askInt(String) int
        +askFloat(String) float
        +askLong(String) long
        +setSession(String, String)
        +buildWorker(Worker) Worker
        +printWorker(Worker)
        +printWorkersList(List)
        +setInputSource(InputSource)
        +close()
    }

    class CommandManager {
        -Map commands
        +get(String) ClientCommand
    }

    class InputSource {
        <<interface>>
        +readLine() String
    }

    class ConsoleInputSource {
        +readLine() String
    }

    class FileInputSource {
        +readLine() String
        +close()
    }

    class ClientCommand {
        <<interface>>
        +execute(String)
    }

    class InsertCommand { +execute(String) }
    class UpdateCommand { +execute(String) }
    class RemoveKeyCommand { +execute(String) }
    class RemoveLowerCommand { +execute(String) }
    class RemoveGreaterKeyCommand { +execute(String) }
    class ClearCommand { +execute(String) }
    class ShowCommand { +execute(String) }
    class InfoCommand { +execute(String) }
    class HelpCommand { +execute(String) }
    class GetWorkerCommand { +execute(String) }
    class FilterContainsNameCommand { +execute(String) }
    class CountLessThanStartDateCommand { +execute(String) }
    class PrintUniqueStartDateCommand { +execute(String) }
    class ReplaceIfLowerCommand { +execute(String) }
    class ExecuteScriptCommand { +execute(String) }

    ConsoleInputSource ..|> InputSource
    FileInputSource ..|> InputSource

    InsertCommand ..|> ClientCommand
    UpdateCommand ..|> ClientCommand
    RemoveKeyCommand ..|> ClientCommand
    RemoveLowerCommand ..|> ClientCommand
    RemoveGreaterKeyCommand ..|> ClientCommand
    ClearCommand ..|> ClientCommand
    ShowCommand ..|> ClientCommand
    InfoCommand ..|> ClientCommand
    HelpCommand ..|> ClientCommand
    GetWorkerCommand ..|> ClientCommand
    FilterContainsNameCommand ..|> ClientCommand
    CountLessThanStartDateCommand ..|> ClientCommand
    PrintUniqueStartDateCommand ..|> ClientCommand
    ReplaceIfLowerCommand ..|> ClientCommand
    ExecuteScriptCommand ..|> ClientCommand

    Client *-- ClientConsole
    Client *-- CommandManager
    Client ..> Request : создаёт
    Client ..> Response : получает
    ClientConsole --> InputSource
    CommandManager o-- ClientCommand

    InsertCommand ..> Request
    InsertCommand ..> WorkerArgument
    UpdateCommand ..> Request
    UpdateCommand ..> IdArgument
    UpdateCommand ..> WorkerArgument
    RemoveKeyCommand ..> Request
    RemoveKeyCommand ..> IdArgument
    FilterContainsNameCommand ..> Request
    FilterContainsNameCommand ..> StringArgument
    CountLessThanStartDateCommand ..> Request
    CountLessThanStartDateCommand ..> StringArgument
    ReplaceIfLowerCommand ..> Request
    ReplaceIfLowerCommand ..> IdArgument
    ReplaceIfLowerCommand ..> WorkerArgument
