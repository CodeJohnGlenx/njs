import java.util.regex.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.FileWriter;

public class Lex {
    public static void main(String[] args) {
        // args[0] expects <filename>.njs
        
        try {
            // convert .njs file into Java String
            String njsContent = Files.readString(Path.of(args[0]));
            String fileExtension = args[0].substring(args[0].indexOf('.'));
            njsContent += " ";
            ArrayList<String[]> lexemeTokenList;

            // throw Exception if file extension is not ".njs"
            if (!fileExtension.equals(".njs")) {
                throw new Exception("Input File only accepts \".njs\" extension!");
            }

            // perform scanning
            lexemeTokenList = scan(njsContent, Tokens.PATTERNLIST);
            if (lexemeTokenList != null) {
                // write lexeme and token to LexOut.txt
                FileWriter lexWriter = new FileWriter("LexOut.txt");

                // true output LexOutReal.txt
                FileWriter lexRealWriter = new FileWriter("LexOut.nlex");

                for (String[] lexemeToken: lexemeTokenList) {
                    lexWriter.write(String.format("%-50s%s\n", lexemeToken[0], lexemeToken[1]));
                    lexRealWriter.write(String.format("%s,%s\n", lexemeToken[0], lexemeToken[1]));
                }
                lexRealWriter.close();
                lexWriter.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Scanning of Token
    // content - the string the will be scanned
    // patternList - a list of regular expressions 
    public static ArrayList<String[]> scan(String content, String[] patternList) {
        Pattern pattern;
        Matcher matcher;
        ArrayList<String[]> lexemeTokenList = new ArrayList<>();  // <lexeme, token>
        Hashtable<String, String> constantList = new Hashtable<>(); 

        // identiferPattern and identifierMatcher for special cases
        Pattern identifierPattern = Pattern.compile("^[_a-zA-Z]+\\w*");
        Matcher identifierMatcher;

        // while content contains possible lexems
        while (!content.isEmpty()) {
            boolean found = false;

            // iterate every regular expressions
            for (String p: patternList) {
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(content);

                // for identifier
                identifierMatcher = identifierPattern.matcher(content);

                // if both regular expression of identifier and some patternList p (e.g. keywords/reserved_words/bool_literal) matches
                if (identifierMatcher.find(0) == true && matcher.find(0) == true) {
                    String identifierSubString = content.substring(identifierMatcher.start(), identifierMatcher.end());
                    String matcherSubString = content.substring(matcher.start(), matcher.end());
                    String equivalentToken = Tokens.TOKEN.get(p);

                    // CONSTANT
                    if (equivalentToken.equals("IDENTIFIER")) {
                        if (lexemeTokenList.size() >= 2) {
                            int lastIndex = lexemeTokenList.size() - 1;
                            String[] lexTokenLast = lexemeTokenList.get(lastIndex);  // assert that this is datatype_KEY
                            String[] lexTokenSecondLast = lexemeTokenList.get(lastIndex - 1);   // assert that this is final keyword
    
                            // identifier must be constant if the last two tokens are <keyword (final)> <datatype_KEY>
                            if (lexTokenLast[1].equals("DATATYPE_KEY") 
                            && lexTokenSecondLast[0].equals("final") && 
                            equivalentToken.equals("IDENTIFIER")) {
                                //equivalentToken = Tokens.TOKEN.get("constant");
                            } 
                        } 
                    }
                    
                    // lexeme is a (keyword/reserved_word/bool_literal)
                    if (identifierSubString.equals(matcherSubString)) {
                        if (lexemeTokenList.size() >= 1) {
                            int lastIndex = lexemeTokenList.size() - 1;
                            String[] lexTokenLast = lexemeTokenList.get(lastIndex);  // assert that this is datatype_KEY

                            // change identifier/constant to reserved_word if lexeme is a keyword/bool_literal/datatype_KEY
                            if (lexTokenLast[1].equals("DATATYPE_KEY") &&
                            (!equivalentToken.equals("IDENTIFIER") && 
                            !equivalentToken.equals("CONSTANT"))) {
                                equivalentToken = Tokens.TOKEN.get("reserved");
                            } 
                        }

                        // replace identifier with constant if there is an existing constant for the current lexeme identifier
                        if (constantList.get(identifierSubString) != null) {
                            equivalentToken = Tokens.TOKEN.get("constant");
                        } 

                        System.out.printf("%-50s%s\n", content.substring(matcher.start(), matcher.end()), equivalentToken);
                        lexemeTokenList.add(new String[] {content.substring(matcher.start(), matcher.end()), equivalentToken});
                        content = content.substring(matcher.end());
                    // lexeme is an identifier
                    // not reachable?
                    } else {

                        String identifierToken = Tokens.TOKEN.get(identifierPattern.pattern());
                        System.out.printf("%-50s%s\n", content.substring(identifierMatcher.start(), identifierMatcher.end()), identifierToken);
                        // 
                        lexemeTokenList.add(new String[] {content.substring(identifierMatcher.start(), identifierMatcher.end()), identifierToken});
                        content = content.substring(identifierMatcher.end());
                    }

                    if (equivalentToken.equals("CONSTANT")) {
                        constantList.put(identifierSubString, equivalentToken);
                    }
                    found = true;
                    break;
                }

                // lexeme belongs to other form of token (e.g. semicolon/increment/int_literal etc.)
                if (matcher.find(0) == true) {
                    if (Tokens.TOKEN.get(p) != "WHITESPACE" && Tokens.TOKEN.get(p) != "SINGLE_LINE_COMMENT" && Tokens.TOKEN.get(p) != "MULTI_LINE_COMMENT") {

                        // break down STRING_LITERAL into different tokens
                        if (Tokens.TOKEN.get(p) == "STRING_LITERAL") {
                            System.out.printf("%-50s%s\n", "\"", Tokens.STRTOKENS.get("open_quote"));
                            lexemeTokenList.add(new String[] {"\"", Tokens.STRTOKENS.get("open_quote")});

                            String strContent = content.substring(matcher.start(), matcher.end());
                            if (strContent.length() > 2) {
                                strContent = strContent.substring(1, strContent.length()-1);
                                while (!strContent.isEmpty()) {
                                    for (String s: Tokens.STRPATTERNLIST) {
                                        Pattern strPattern = Pattern.compile(s);
                                        Matcher strMatcher = strPattern.matcher(strContent);
    
                                        if (strMatcher.find(0) == true) {
                                            System.out.printf("%-50s%s\n", strContent.substring(strMatcher.start(), strMatcher.end()), Tokens.STRTOKENS.get(s));
                                            lexemeTokenList.add(new String[] {strContent.substring(strMatcher.start(), strMatcher.end()), Tokens.STRTOKENS.get(s)});
                                            strContent = strContent.substring(strMatcher.end());
                                            break;
                                        } else {
                                        }
                                    }
                                }
                            }

                            System.out.printf("%-50s%s\n", "\"", Tokens.STRTOKENS.get("close_quote"));
                            lexemeTokenList.add(new String[] {"\"", Tokens.STRTOKENS.get("close_quote")});

                        // break down CHAR_LITERAL into different tokens
                        } else  if (Tokens.TOKEN.get(p) == "CHAR_LITERAL") {
                            System.out.printf("%-50s%s\n", "\'", Tokens.CHARTOKENS.get("open_quote"));
                            lexemeTokenList.add(new String[] {"\'", Tokens.CHARTOKENS.get("open_quote")});
                        
                            String charContent = content.substring(matcher.start(), matcher.end());
                            if (charContent.length() > 2) {
                                charContent = charContent.substring(1, charContent.length()-1);
                                while (!charContent.isEmpty()) {
                                    for (String s: Tokens.CHARPATTERNLIST) {
                                        Pattern charPattern = Pattern.compile(s);
                                        Matcher charMatcher = charPattern.matcher(charContent);
                        
                                        if (charMatcher.find(0) == true) {
                                            System.out.printf("%-50s%s\n", charContent.substring(charMatcher.start(), charMatcher.end()), Tokens.CHARTOKENS.get(s));
                                            lexemeTokenList.add(new String[] {charContent.substring(charMatcher.start(), charMatcher.end()), Tokens.CHARTOKENS.get(s)});
                                            charContent = charContent.substring(charMatcher.end());
                                            break;
                                        } else {
                                        }
                                    }
                                }
                            }
                        
                            System.out.printf("%-50s%s\n", "\'", Tokens.CHARTOKENS.get("close_quote"));
                            lexemeTokenList.add(new String[] {"\'", Tokens.CHARTOKENS.get("close_quote")});
                        } 

                        // break down single line comment 
                        else if (Tokens.TOKEN.get(p) == "SINGLE_LINE_COMMENT") {
                            System.out.printf("%-50s%s\n", "#", Tokens.SINGLECOMMENTTOKENS.get("comment"));
                            lexemeTokenList.add(new String[] {"#", Tokens.SINGLECOMMENTTOKENS.get("comment")});
                            String commentStr = content.substring(matcher.start() + 1, matcher.end());

                            while (!commentStr.isEmpty()) {
                                for (String s: Tokens.SINGLECOMMENTPATTERNLIST) {
                                    Pattern commentPattern = Pattern.compile(s);
                                    Matcher commentMatcher = commentPattern.matcher(commentStr);
                    
                                    if (commentMatcher.find(0) == true) {
                                        System.out.printf("%-50s%s\n", commentStr.substring(commentMatcher.start(), commentMatcher.end()), Tokens.SINGLECOMMENTTOKENS.get(s));
                                        lexemeTokenList.add(new String[] {commentStr.substring(commentMatcher.start(), commentMatcher.end()), Tokens.SINGLECOMMENTTOKENS.get(s)});
                                        commentStr = commentStr.substring(commentMatcher.end());
                                        break;
                                    } else {
                                    }
                                }
                            }
                        }

                        // break down multi line comment 
                        else if (Tokens.TOKEN.get(p) == "MULTI_LINE_COMMENT") {
                            System.out.printf("%-50s%s\n", "'''", Tokens.MULTICOMMENTTOKENS.get("open_multi"));
                            lexemeTokenList.add(new String[] {"'''", Tokens.MULTICOMMENTTOKENS.get("open_multi")});
                            String commentStr = content.substring(matcher.start() + 3, matcher.end() - 3);
                            
                            while (!commentStr.isEmpty()) {
                                for (String s: Tokens.MULTICOMMENTPATTERNLIST) {
                                    Pattern commentPattern = Pattern.compile(s);
                                    Matcher commentMatcher = commentPattern.matcher(commentStr);
                                    if (commentMatcher.find(0) == true) {
                                        if (Tokens.MULTICOMMENTTOKENS.get(s) != "NEWLINE") {
                                            System.out.printf("%-50s%s\n", commentStr.substring(commentMatcher.start(), commentMatcher.end()), Tokens.MULTICOMMENTTOKENS.get(s));
                                            lexemeTokenList.add(new String[] {commentStr.substring(commentMatcher.start(), commentMatcher.end()), Tokens.MULTICOMMENTTOKENS.get(s)});
                                        }
                                        commentStr = commentStr.substring(commentMatcher.end());
                                        break;
                                    }

                                }
                            }


                            System.out.printf("%-50s%s\n", "'''", Tokens.MULTICOMMENTTOKENS.get("close_multi"));
                            lexemeTokenList.add(new String[] {"'''", Tokens.MULTICOMMENTTOKENS.get("close_multi")});
                        }
                        
                        else {
                            String equivalentToken = Tokens.TOKEN.get(p);
                            System.out.printf("%-50s%s\n", content.substring(matcher.start(), matcher.end()), equivalentToken);
                            lexemeTokenList.add(new String[] {content.substring(matcher.start(), matcher.end()), equivalentToken});
                        }
                    }
                    content = content.substring(matcher.end());
                    found = true;
                    break;
                } 

            }
            
            // symbol or lexeme is unrecognizable
            if (found == false) {
                String error = String.format("%s", content.charAt(0));
                System.out.printf("%-50s%s\n", error, "NOT_RECOGNIZED_AS_TOKEN");
                System.out.println("\n/*Lex Scanner Interrupted.*/\n");

                //return new ArrayList<>() {{add(new String[] {error, "NOT_RECOGNIZED_AS_TOKEN"});}};
                return null;
            }
        }
        return lexemeTokenList;
    }
}

