/*3. (For Advanced Learners – home) Template filler (TemplateFiller.java +
Driver.java)
(a) In Driver.main, define a template like “Dear {name}, order {id} ships {date}.” and
two parallel arrays: names {“name”, “id”} and values {“Riya”, “A07”} (note {date}
is not supplied).
(b) Compile a Pattern for \{(\w+)\} and get a Matcher on the template.
(c) Walk the matches with a StringBuilder: for each {placeholder}, look up its value in
the names array and replace with the value, or with “[?]” if not found.
(d) Print the filled template.
Expected output: the unsupplied {date} becomes [?].*/

import java.util.regex.*;

public class TemplateFiller {

    public static String fillTemplate(String template, String[] names, String[] values) {

        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(template);

        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        while (m.find()) {

            // Add text before the placeholder
            sb.append(template.substring(lastIndex, m.start()));

            // Get placeholder name (like name, id, date)
            String key = m.group(1);

            String replacement = "[?]";

            // Search for key in names array
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(key)) {
                    replacement = values[i];
                    break;
                }
            }

            // Add replacement
            sb.append(replacement);

            lastIndex = m.end();
        }

        // Add remaining part of template
        sb.append(template.substring(lastIndex));

        return sb.toString();
    }
}