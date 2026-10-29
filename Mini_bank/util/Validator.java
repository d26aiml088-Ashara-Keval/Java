package util;

import exception.InvalidAmountException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Validator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validator() {
    }

    public static List<String> validateAccount(String ownerName, String mobile,
            String email, long openingBalance) {
        List<String> errors = new ArrayList<>();
        if (ownerName == null || ownerName.isBlank() || ownerName.length() > 50) {
            errors.add("Owner name must contain 1 to 50 characters");
        }
        if (mobile == null || !MOBILE_PATTERN.matcher(mobile).matches()) {
            errors.add("Mobile must be a valid 10-digit Indian number");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email address is invalid");
        }
        if (openingBalance < 0) {
            errors.add("Opening balance must not be negative");
        }
        return errors;
    }

    public static void validateAmount(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
