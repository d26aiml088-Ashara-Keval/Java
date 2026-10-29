package model;

import exception.BankException;
import service.BankService;
import service.TransactionProcessor;
import service.TransactionProcessor.TransactionRequest;
import util.BankInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class MiniBank {
    private MiniBank() {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path dataDirectory = args.length == 0 ? Paths.get("data") : Paths.get(args[0]);
        BankService bank = new BankService(dataDirectory);
        TransactionProcessor processor = new TransactionProcessor(bank);
        BankInfo bankInfo = bank.getBankInfo();
        System.out.println(bankInfo.name() + " - " + bankInfo.branch());
        System.out.println("Loaded " + bank.listAccounts().size() + " saved account(s).");

        boolean interrupted = false;
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choiceLine = readLine(scanner, "Enter choice: ");
                if (choiceLine == null) {
                    break;
                }
                try {
                    int choice = Integer.parseInt(choiceLine.trim());
                    switch (choice) {
                        case 1 -> openAccount(scanner, bank, processor);
                        case 2 -> deposit(scanner, bank);
                        case 3 -> withdraw(scanner, bank);
                        case 4 -> transfer(scanner, bank);
                        case 5 -> printStatement(scanner, bank);
                        case 6 -> printAccounts(bank);
                        case 7 -> processBatch(scanner, processor);
                        case 8 -> System.out.println(bank.generateReport());
                        case 9 -> running = false;
                        default -> System.out.println("Invalid choice. Select 1 through 9.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Enter a valid whole number.");
                } catch (BankException | IllegalArgumentException exception) {
                    System.out.println("Operation failed: " + exception.getMessage());
                } catch (IOException exception) {
                    System.out.println("File operation failed: " + exception.getMessage());
                } catch (InterruptedException exception) {
                    interrupted = true;
                    running = false;
                    System.out.println("Batch wait interrupted; stopping after queued work completes.");
                }
            }
        }

        try {
            processor.stop();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while waiting for transactions to finish", exception);
        }
        bank.save();
        System.out.println("Accounts saved. Thanks for using MiniBank.");
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Statement");
        System.out.println("6. List Accounts");
        System.out.println("7. Process Transaction Batch");
        System.out.println("8. End-of-day Report");
        System.out.println("9. Exit");
    }

    private static void openAccount(Scanner scanner, BankService bank,
            TransactionProcessor processor) throws BankException {
        String name = requiredLine(scanner, "Owner name: ");
        String mobile = requiredLine(scanner, "Mobile: ");
        String email = requiredLine(scanner, "Email: ");
        long openingBalance = readAmount(scanner, "Opening balance: ");
        Account account = bank.openAccount(name, mobile, email, openingBalance);
        processor.register(account);
        System.out.println("Account opened: " + account.getAccountNumber());
    }

    private static void deposit(Scanner scanner, BankService bank) throws BankException, IOException {
        String accountNumber = requiredLine(scanner, "Account number: ");
        long amount = readAmount(scanner, "Deposit amount: ");
        bank.deposit(accountNumber, amount);
        System.out.println("Deposit completed.");
    }

    private static void withdraw(Scanner scanner, BankService bank)
            throws BankException, IOException {
        String accountNumber = requiredLine(scanner, "Account number: ");
        long amount = readAmount(scanner, "Withdrawal amount: ");
        bank.withdraw(accountNumber, amount);
        System.out.println("Withdrawal completed.");
    }

    private static void transfer(Scanner scanner, BankService bank)
            throws BankException, IOException {
        String source = requiredLine(scanner, "From account: ");
        String destination = requiredLine(scanner, "To account: ");
        long amount = readAmount(scanner, "Transfer amount: ");
        bank.transfer(source, destination, amount);
        System.out.println("Transfer completed.");
    }

    private static void printStatement(Scanner scanner, BankService bank) throws BankException {
        System.out.println(bank.statement(requiredLine(scanner, "Account number: ")));
    }

    private static void printAccounts(BankService bank) {
        List<Account> accounts = bank.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts are open.");
            return;
        }
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    private static void processBatch(Scanner scanner, TransactionProcessor processor)
            throws BankException, IOException, InterruptedException {
        int count = readCount(scanner, "Number of transactions: ");
        List<TransactionRequest> requests = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String type = requiredLine(scanner, "Transaction " + (i + 1)
                    + " type (DEPOSIT/WITHDRAW): ").trim().toUpperCase(java.util.Locale.ROOT);
            TransactionRequest.Type transactionType = switch (type) {
                case "DEPOSIT" -> TransactionRequest.Type.DEPOSIT;
                case "WITHDRAW", "WITHDRAWAL" -> TransactionRequest.Type.WITHDRAW;
                default -> throw new IllegalArgumentException("Unknown batch type: " + type);
            };
            String accountNumber = requiredLine(scanner, "Account number: ");
            long amount = readAmount(scanner, "Amount: ");
            requests.add(new TransactionRequest(transactionType, accountNumber, amount));
        }
        processor.processBatch(requests);
        System.out.println("Batch completed (" + count + " transaction(s)).");
    }

    private static long readAmount(Scanner scanner, String prompt) {
        String value = requiredLine(scanner, prompt);
        try {
            long amount = Long.parseLong(value.trim());
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must not be negative");
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a valid whole-number amount", exception);
        }
    }

    private static int readCount(Scanner scanner, String prompt) {
        String value = requiredLine(scanner, prompt);
        try {
            int count = Integer.parseInt(value.trim());
            if (count < 0 || count > 10_000) {
                throw new IllegalArgumentException("Transaction count must be between 0 and 10000");
            }
            return count;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a valid transaction count", exception);
        }
    }

    private static String requiredLine(Scanner scanner, String prompt) {
        String value = readLine(scanner, prompt);
        if (value == null) {
            throw new IllegalArgumentException("Input ended before the operation was complete");
        }
        return value;
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
