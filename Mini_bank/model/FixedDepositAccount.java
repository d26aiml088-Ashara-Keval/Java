package model;

public class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(Customer customer, long openingBalance) {
        super(customer, openingBalance);
    }

    @Override
    protected boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    public double interestRate() {
        return 7.0;
    }
}
