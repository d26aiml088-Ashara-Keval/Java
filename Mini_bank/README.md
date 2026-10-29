# MiniBank

MiniBank is a Java 17 console banking application organized into the same clear
`model`, `service`, `util`, `exception`, and `test` package groups used by the
[Java Mini Bank practical reference](https://github.com/RishitChitroda827/Java-Mini-Bank-089).
The implementation here is an independent Maven project and retains the JUnit tests and
safer transaction handling from the capstone.

## Project structure

```text
Mini_bank/
├── exception/          Checked banking and validation exceptions
├── model/              Serializable accounts, customers, account types, and menu
│   └── annotation/     Runtime metadata annotations
├── service/            Bank service, account store, and concurrency helpers
├── util/               Repository, validation, persistence, statements, and reports
├── test/               JUnit 5 tests
├── manifest.txt        Main class for the plain JDK JAR build
├── MiniBank.jar        Runnable application JAR
├── pom.xml             Maven build and test configuration
└── README.md
```

## Features

- Open and persist accounts; deposit, withdraw, transfer, list accounts, and print
  transaction statements from the interactive menu.
- Account subclasses for savings, current, and fixed-deposit account behavior.
- A reflection-based generic repository keyed by `@Id`, plus collection-backed account
  storage and account-number ordering.
- Checked banking exceptions, input validation, and optional reflective audit metadata.
- Thread-safe account operations, deterministic account lock ordering, and fixed-pool batch
  processing backed by a `ConcurrentHashMap`.
- Append-only daily transaction logs and an NIO end-of-day report containing transaction
  totals and log-file details.
- Java object serialization for account state and three JUnit tests, including concurrent
  deposit verification.

## Build and test with Maven

From this directory:

```powershell
mvn clean test package
```

The runnable JAR is written to `MiniBank.jar`.

## Build with the JDK only

Compile the application packages:

```powershell
javac -d out (Get-ChildItem exception,model,service,util -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

Create and run the JAR:

```powershell
jar cfm MiniBank.jar manifest.txt -C out .
java -jar MiniBank.jar
```

## Run

```powershell
java -jar MiniBank.jar
```

By default, saved state, daily logs, and the end-of-day report are written under `data/`
relative to the current working directory. Supply a different data directory as the first
argument to change the storage location:

```powershell
java -jar MiniBank.jar C:\path\to\minibank-data
```

The application loads accounts at startup and waits for submitted batch work before saving
state on exit. The mobile-number validator accepts the 10-digit Indian format used in the
practicals.
