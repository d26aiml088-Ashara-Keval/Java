package model;

public class SavingsAccount extends Account implements BankInterfaces.Premium {
    private static final long serialVersionUID = 1L;

    private final long minimumBalance;

    public SavingsAccount(Customer customer, long openingBalance, long minimumBalance) {
        super(customer, openingBalance);
        if (minimumBalance < 0 || minimumBalance > openingBalance) {
            throw new IllegalArgumentException(
                    "Minimum balance must be non-negative and no greater than opening balance");
        }
        this.minimumBalance = minimumBalance;
    }

    public long getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    protected boolean canWithdraw(long amount) {
        return amount <= getBalance() - minimumBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }
}
