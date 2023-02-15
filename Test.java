import java.util.regex.*;

public class Test {
    public static void main(String[] args) {
        String string = ";;=value = 200; _variable\"\\\"\"";
        System.out.println("String: " + string + "\n");
        scan(string, Tokens.PATTERNLIST);
    }

    public static String scan(String content, String[] patternList) {
        Pattern pattern;
        Matcher matcher;

        while (!content.isEmpty()) {
            boolean found = false;

            for (String p: patternList) {
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(content);

                if (matcher.find() == true) {
                    if (Tokens.TOKEN.get(p) != "WHITESPACE") {
                        System.out.printf("%s\t\t\t%s\n", content.substring(matcher.start(), matcher.end()), Tokens.TOKEN.get(p));
                     }
                    content = content.substring(matcher.end());
                    found = true;
                    break;
                } 
            }
             

            if (found == false) {
                System.out.printf("%s not found\n", content.charAt(0));
                return "Nothing";
            }
        }
        return "Done";
    }


}


/*
 * Fix patternList error
 */

 /*
  *         
        String regex = "^;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find() == true) {
            System.out.println("Regex Found!");
            System.out.println(matcher.start());
            System.out.println(matcher.end());

        } else {
            System.out.println("Regex Not Found!");
        }
  */

