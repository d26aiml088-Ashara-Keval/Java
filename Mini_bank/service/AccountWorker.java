package service;

import model.Account;

public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        if (account == null || times < 0 || amount <= 0) {
            throw new IllegalArgumentException("Account, positive amount, and valid count required");
        }
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " started.");
        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (exception.BankException failure) {
                throw new IllegalStateException("Worker deposit failed", failure);
            }
        }
        System.out.println(name + " finished.");
    }
}
