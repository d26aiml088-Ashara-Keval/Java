/*
1. Password strength (PasswordChecker.java + Driver.java)
(a) In PasswordChecker, check four rules — length ≥ 8, contains an uppercase letter,
contains a digit, contains a special character — using regex (e.g. “.*[A-Z].*”) or
String methods.
(b) Write strength(String pw) that counts how many rules pass and returns “Weak” (0–
1), “Medium” (2–3) or “Strong” (4).
(c) In Driver.main: test several passwords; for each, print which rules passed and the
final label.
Expected output: “abc” → Weak; “Abcd1234!” → Strong.
 */
public class Driver1 {
    public static void main(String[] args) {
        String str1, str2 ;
        str1= "abcd";
        str2="ABCDEFG@1";
        passwordChecker pw = new passwordChecker();
        System.out.println(pw.PasswordChecker(str1));
        System.out.println(pw.PasswordChecker(str2));
        
    }
}
