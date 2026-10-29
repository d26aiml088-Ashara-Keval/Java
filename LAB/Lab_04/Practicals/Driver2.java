import java.util.Scanner;

public class Driver2 {
    public static void main(String[] args) {

        // Chat logs (including one malformed line)
        String[] logs = {
            "10:05 Keval Hellow",
            "11:29 Harsh Hi",
            "1:56 Keval "
        };

        // Read keyword from user
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        // Call filter method
        String result = ChatFilter.filterLogs(logs, keyword);

        // Print result
        System.out.println(result);

        sc.close();
    }
}
