package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.ValidationException;
import model.Account;
import model.annotation.Audit;
import util.AuditLogger;
import util.BankInfo;
import util.ReportGenerator;
import util.Repository;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Repository<Account> accounts = new Repository<>();
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();
    private final Path dataDirectory;
    private final Path stateFile;
    private final Path logsDirectory;
    private final BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");

    public BankService(Path dataDirectory) throws IOException, ClassNotFoundException {
        if (dataDirectory == null) {
            throw new IllegalArgumentException("Data directory must not be null");
        }
        this.dataDirectory = dataDirectory;
        stateFile = dataDirectory.resolve("accounts.dat");
        logsDirectory = dataDirectory.resolve("logs");
        Files.createDirectories(logsDirectory);
        if (Files.exists(stateFile)) {
            for (Account account : StatePersister.load(stateFile)) {
                addAccount(account);
            }
        }
    }

    @Audit
    public Account openAccount(String ownerName, String mobile, String email, long openingBalance)
            throws ValidationException {
        AuditLogger.record(this, "openAccount", String.class, String.class, String.class, long.class);
        List<String> errors = Validator.validateAccount(ownerName, mobile, email, openingBalance);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Account account = new Account(ownerName.trim(), mobile, email, openingBalance);
        addAccount(account);
        return account;
    }

    @Audit
    public void deposit(String accountNumber, long amount)
            throws BankException, IOException {
        AuditLogger.record(this, "deposit", String.class, long.class);
        Account account = requireAccount(accountNumber);
        Validator.validateAmount(amount);
        account.deposit(amount);
        appendLog("DEPOSIT", amount);
    }

    @Audit
    public void withdraw(String accountNumber, long amount)
            throws BankException, IOException {
        AuditLogger.record(this, "withdraw", String.class, long.class);
        Account account = requireAccount(accountNumber);
        Validator.validateAmount(amount);
        account.withdraw(amount);
        appendLog("WITHDRAWAL", amount);
    }

    @Audit
    public void transfer(String sourceNumber, String destinationNumber, long amount)
            throws BankException, IOException {
        AuditLogger.record(this, "transfer", String.class, String.class, long.class);
        if (sourceNumber == null || destinationNumber == null) {
            throw new AccountNotFoundException("null");
        }
        Account source = requireAccount(sourceNumber);
        Account destination = requireAccount(destinationNumber);
        Validator.validateAmount(amount);
        if (source == destination) {
            throw new BankException("Source and destination must be different accounts");
        }
        Account.transfer(source, destination, amount);
        TransactionLog.appendAll(logFile(), List.of(
                "WITHDRAWAL," + amount, "DEPOSIT," + amount));
    }

    @Audit
    public String statement(String accountNumber) throws AccountNotFoundException {
        AuditLogger.record(this, "statement", String.class);
        Account account = requireAccount(accountNumber);
        return StatementFormatter.format(account);
    }

    @Audit
    public ArrayList<Account> listAccounts() {
        AuditLogger.record(this, "listAccounts");
        ArrayList<Account> allAccounts = accounts.findAll();
        allAccounts.sort(Comparator.naturalOrder());
        return allAccounts;
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        return requireAccount(accountNumber);
    }

    public String generateReport() throws IOException {
        return ReportGenerator.generate(logsDirectory, dataDirectory.resolve("end-of-day-report.txt"));
    }

    public void save() throws IOException {
        StatePersister.save(listAccounts().toArray(Account[]::new), stateFile);
    }

    public List<Account> snapshot() {
        return List.copyOf(accounts.findAll());
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    private void addAccount(Account account) {
        accounts.save(account);
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        if (accountNumber == null) {
            throw new AccountNotFoundException("null");
        }
        Account account = sharedAccounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void appendLog(String type, long amount) throws IOException {
        TransactionLog.append(logFile(), type + "," + amount);
    }

    private Path logFile() {
        return logsDirectory.resolve(LocalDate.now() + "-transactions.log");
    }
}
