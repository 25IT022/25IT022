import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TemplateFiller {

    public String fillTemplate(String template, String[] names, String[] values) {

        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(template);

        StringBuilder result = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {

            // Add text before the placeholder
            result.append(template.substring(lastIndex, matcher.start()));

            // Extract placeholder name
            String key = matcher.group(1);

            String replacement = "[?]";

            // Search for the placeholder in names[]
            for (int i = 0; i < names.length; i++) {

                if (names[i].equals(key)) {
                    replacement = values[i];
                    break;
                }
            }

            // Add replacement
            result.append(replacement);

            // Move to next part of the template
            lastIndex = matcher.end();
        }

        // Add remaining text
        result.append(template.substring(lastIndex));

        return result.toString();
    }
}

public class Driver_5 {

    public static void main(String[] args) {

        String template = "Dear {name}, order {id} ships {date}.";

        String[] names = {"name", "id"};
        String[] values = {"Riya", "A07"};

        TemplateFiller tf = new TemplateFiller();

        String output = tf.fillTemplate(template, names, values);

        System.out.println(output);
    }
}