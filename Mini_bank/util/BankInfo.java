package util;

public record BankInfo(String name, String branch) {
    public BankInfo {
        if (name == null || name.isBlank() || branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Bank name and branch are required");
        }
    }
}
