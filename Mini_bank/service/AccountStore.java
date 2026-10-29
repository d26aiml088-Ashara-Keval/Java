package service;

import model.Account;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public synchronized void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    public synchronized Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized ArrayList<Account> sortedByBalance() {
        ArrayList<Account> result = getAll();
        result.sort(Comparator.comparingLong(Account::getBalance)
                .thenComparing(Account::getAccountNumber));
        return result;
    }

    public synchronized void recordTransaction(String transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must not be null");
        }
        transactionHistory.add(transaction);
    }

    public synchronized List<String> getTransactionHistory() {
        return List.copyOf(transactionHistory);
    }
}
