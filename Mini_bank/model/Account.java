package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable, Comparable<Account>, BankInterfaces.Transactable,
        BankInterfaces.InterestBearing {
    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;
    private final Customer customer;
    private long balance;
    private final ArrayList<String> transactions = new ArrayList<>();

    public Account(String ownerName, String mobile, String email, long openingBalance) {
        this(new Customer(ownerName, mobile, email), openingBalance);
    }

    public Account(Customer customer, long openingBalance) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance must not be negative");
        }
        accountNumber = "MB" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 12).toUpperCase(java.util.Locale.ROOT);
        this.customer = customer;
        balance = openingBalance;
        transactions.add("OPENING," + openingBalance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return customer.getName();
    }

    public String getMobile() {
        return customer.getMobile();
    }

    public String getEmail() {
        return customer.getEmail();
    }

    public Customer getCustomer() {
        return customer;
    }

    public synchronized long getBalance() {
        return balance;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        requirePositive(amount);
        final long updatedBalance;
        try {
            updatedBalance = Math.addExact(balance, amount);
        } catch (ArithmeticException exception) {
            throw new InvalidAmountException("Deposit exceeds the supported balance limit");
        }
        balance = updatedBalance;
        transactions.add("DEPOSIT," + amount);
    }

    @Override
    public synchronized void withdraw(long amount)
            throws InvalidAmountException, InsufficientFundsException {
        requirePositive(amount);
        if (!canWithdraw(amount)) {
            long shortfall;
            try {
                shortfall = Math.subtractExact(amount, balance);
            } catch (ArithmeticException exception) {
                shortfall = Long.MAX_VALUE;
            }
            throw new InsufficientFundsException(shortfall);
        }
        balance -= amount;
        transactions.add("WITHDRAWAL," + amount);
    }

    protected boolean canWithdraw(long amount) {
        return amount <= balance;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    public static void transfer(Account source, Account destination, long amount)
            throws InvalidAmountException, InsufficientFundsException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Transfer accounts must not be null");
        }
        if (source == destination) {
            throw new IllegalArgumentException("Source and destination must be different accounts");
        }
        requirePositive(amount);

        Account first = source.accountNumber.compareTo(destination.accountNumber) < 0
                ? source : destination;
        Account second = first == source ? destination : source;
        synchronized (first) {
            synchronized (second) {
                if (!source.canWithdraw(amount)) {
                    long shortfall;
                    try {
                        shortfall = Math.subtractExact(amount, source.balance);
                    } catch (ArithmeticException exception) {
                        shortfall = Long.MAX_VALUE;
                    }
                    throw new InsufficientFundsException(shortfall);
                }
                final long updatedDestinationBalance;
                try {
                    updatedDestinationBalance = Math.addExact(destination.balance, amount);
                } catch (ArithmeticException exception) {
                    throw new InvalidAmountException(
                            "Transfer exceeds the destination balance limit");
                }
                source.balance -= amount;
                destination.balance = updatedDestinationBalance;
                source.transactions.add("TRANSFER_OUT," + amount + "," + destination.accountNumber);
                destination.transactions.add("TRANSFER_IN," + amount + "," + source.accountNumber);
            }
        }
    }

    public synchronized List<String> getTransactions() {
        return List.copyOf(transactions);
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public synchronized String toString() {
        return getClass().getSimpleName() + "{accountNumber='" + accountNumber
                + "', customer=" + customer + ", balance=" + balance + "}";
    }

    private static void requirePositive(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
