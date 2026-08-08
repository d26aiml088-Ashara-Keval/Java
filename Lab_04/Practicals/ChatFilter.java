/*2. Chat-log filter (ChatFilter.java + Driver.java)
(a) In Driver.main, define a String[] of log lines like “10:05 alice Hello there” (include
one malformed line with fewer than 3 parts).
(b) Read a keyword from the user.
(c) For each line, split into time/user/message with split(“ ”, 3); skip lines that do not
have 3 parts.
(d) If the message contains the keyword (case-insensitive), increment a counter and
append “time user: message” to a StringBuilder.
(e) Print “Matches: N” and the built report.
Expected output: keyword “hello” over 3 lines (1 malformed) → “Matches: 1” and
the matching line.*/
public class ChatFilter {

    public static String filterLogs(String[] logs, String keyword) {
        int count = 0;
        StringBuilder sb = new StringBuilder();

        for (String line : logs) {

            // Split into max 3 parts: time, user, message
            String[] parts = line.split(" ", 3);

            // Skip malformed lines
            if (parts.length < 3) {
                continue;
            }

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            // Case-insensitive keyword check
            if (message.toLowerCase().contains(keyword.toLowerCase())) {
                count++;
                sb.append(time + " " + user + ": " + message + "\n");
            }
        }

        return "Matches: " + count + "\n" + sb.toString();
    }
}