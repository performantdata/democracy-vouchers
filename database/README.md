# Democracy vouchers database

This subproject contains [a manually-maintained database description](democracy-vouchers.changelog.xml)
(in the form of a [Liquibase changelog][Liquibase-changelog])
from which the build generates:
- a database schema
- a library of Scala [Slick][Slick] classes for accessing the database

## database schema

```mermaid
---
title: authN/Z class diagram
---
classDiagram
    class AuthenticationSubject {
        <<Abstract>>
        +id : Long
    }

    class AuthenticationPrincipal {
        +username : String
        +passwordHashed : String
    }
    AuthenticationPrincipal "*" --* AuthenticationSubject
    
    class AuthenticationGroup {
        +id : Long
        +name : String
    }
    AuthenticationGroup "*" --o "*" AuthenticationSubject
    
    class AuthenticationRole {
        +id : Long
        +name : String
    }
    AuthenticationRole "*" -- "*" AuthenticationGroup
    
    class Service {
        +id : Long
        +name : String
    }
    Service --|> AuthenticationSubject
    
    class Person {
        +id : Long
    }
    Person --|> AuthenticationSubject
```
```mermaid
---
title: person class diagram
---
classDiagram
    class Person {
        +id : Long
    }
    
    class PersonalName {
        +fullName : String
        +fullNameLatinized : String
    }
    Person *-- "+names 1..*" PersonalName

    class ResidentialAddress {
        +streetAddress : String
        +city : String
        +postalCode : String
    }

    class Residence {
        +start : Date
        +end : Option[Date]
    }
    Residence "1..*" --o Person
    Residence -- ResidentialAddress

    class GovernmentOffice {
        +title : String
    }
    
    class ElectionCandidate {
        +certified : Boolean
    }
    ElectionCandidate --|> Person
    ElectionCandidate "*" -- "1..*" GovernmentOffice
    
    class EligibleDonor
    EligibleDonor --|> Person
    
    class DemocracyVoucher {
        +amount : Money
    }
    DemocracyVoucher "4" --* EligibleDonor
    DemocracyVoucher "*" --o ElectionCandidate
```

## generates database-specific code

Most examples of [the Slick code generation tool][Slick-codegen] use an in-memory instance of the H2 database server
as a target for generating the database schema that Slick reads back via JDBC.
The shortcoming to that is that Slick _could_ generate different code
depending on which database software it reads from.
So the code generated from H2 may not work correctly with MySQL, for example.

This subproject is different in that one configures into the build,
for which database software type(s)—PostgreSQL, MySQL, Oracle, etc.—one wants to generate code.
For each such type it creates an instance of the database server and generates the corresponding code,
one such library for each database type.

[Liquibase-changelog]: https://docs.liquibase.com/concepts/changelogs/home.html
[Slick]: https://scala-slick.org/
[Slick-codegen]: https://scala-slick.org/doc/3.5.0/code-generation.html
