package model;

public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    private final long overdraftLimit;

    public CurrentAccount(Customer customer, long openingBalance, long overdraftLimit) {
        super(customer, openingBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit must not be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    protected boolean canWithdraw(long amount) {
        try {
            return Math.subtractExact(getBalance(), amount) >= -overdraftLimit;
        } catch (ArithmeticException exception) {
            return false;
        }
    }
}
