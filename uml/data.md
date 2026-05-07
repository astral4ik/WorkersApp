classDiagram
    class Worker {
        <<Serializable>>
        -int id
        -String name
        -Coordinates coordinates
        -LocalDateTime creationDate
        -int salary
        -LocalDateTime startDate
        -Position position
        -Status status
        -Organization organization
        -String ownerLogin
        +compareTo(Worker) int
        +toString() String
    }

    class Coordinates {
        <<Serializable>>
        -int x
        -float y
    }

    class Organization {
        <<Serializable>>
        -String fullName
        -Integer annualTurnover
        -Address officialAddress
        -int employeesCount
    }

    class Address {
        <<Serializable>>
        -String street
        -Location town
    }

    class Location {
        <<Serializable>>
        -long x
        -Integer y
        -Integer z
    }

    class Position {
        <<enumeration>>
        MANAGER
        LABORER
        BAKER
    }

    class Status {
        <<enumeration>>
        FIRED
        HIRED
        RECOMMENDED_FOR_PROMOTION
        REGULAR
        PROBATION
    }

    Worker *-- Coordinates : coordinates
    Worker o-- Organization : organization
    Worker --> Position : position
    Worker --> Status : status
    Organization *-- Address : officialAddress
    Address o-- Location : town
