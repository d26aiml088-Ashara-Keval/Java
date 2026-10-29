package model;

import exception.BankException;

public final class BankInterfaces {
    private BankInterfaces() {
    }

    public interface Transactable {
        void deposit(long amount) throws BankException;

        void withdraw(long amount) throws BankException;
    }

    public interface InterestBearing {
        double interestRate();

        default double yearlyInterest(long balance) {
            return balance * interestRate() / 100.0;
        }
    }

    public interface Premium {
    }

    @FunctionalInterface
    public interface WithdrawRule {
        boolean allow(Account account, long amount) throws BankException;
    }
}
