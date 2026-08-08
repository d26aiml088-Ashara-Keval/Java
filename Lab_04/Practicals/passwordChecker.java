/*1. Password strength (PasswordChecker.java + Driver.java)
   (a) In PasswordChecker, check four rules — length ≥ 8, contains an uppercase letter,
   contains a digit, contains a special character — using regex (e.g. “.*[A-Z].*”) or
   String methods.
   (b) Write strength(String pw) that counts how many rules pass and returns “Weak” (0–
   1\), “Medium” (2–3) or “Strong” (4).
   (c) In Driver.main: test several passwords; for each, print which rules passed and the
   final label.
   Expected output: “abc” → Weak; “Abcd1234!” → Strong.
*/ 
public class passwordChecker {
    int count=0;
    public int PasswordChecker(String pw){
        if (pw.length() >= 8){
            count++;
        }
        if (pw.matches(".*[A-Z].*")){
            count++;
        }
        if (pw.matches(".*[0-9].*")){
            count++;
        }
        if (pw.matches(".*[^a-zA-Z0-9].*")){
            count++;
        }
        return count;
    }
}
