package util;

import model.Account;

public final class StatementFormatter {
    private StatementFormatter() {
    }

    public static String format(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        StringBuilder statement = new StringBuilder("Account Statement\n")
                .append("-----------------\n")
                .append("Account number: ").append(account.getAccountNumber()).append('\n')
                .append("Owner: ").append(account.getOwnerName()).append('\n')
                .append("Mobile: ").append(account.getMobile()).append('\n')
                .append("Email: ").append(account.getEmail()).append('\n')
                .append("Balance: ").append(account.getBalance()).append('\n')
                .append("Transactions:\n");
        for (String transaction : account.getTransactions()) {
            statement.append("  ").append(transaction).append('\n');
        }
        return statement.toString();
    }
}
