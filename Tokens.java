import java.util.Hashtable;

public class Tokens {

    // list of regular expressions
    public static final String[] PATTERNLIST = {
        // comments
        "^#.*",  // SINGLE_LINE_COMMENT
        "^'''(.|[\r\n]|)*?'''",  // MULTI_LINE_COMMENT

        // data types
        "^String",  // DATATYPE_KEY
        "^int",  // DATATYPE_KEY
        "^char",  // DATATYPE_KEY
        "^float",  // DATATYPE_KEY
        "^bool",   // DATATYPE_KEY

        // reserved word (for now)
        "^double",  // RESERVED_WORD

        // keywords
        "^input",  // KEYWORD
        "^in",  // KEYWORD
        "^not",  // KEYWORD
        "^try",  // KEYWORD
        "^except",  // KEYWORD
        "^return",  // KEYWORD
        "^elif",  // KEYWORD
        "^else",  // KEYWORD
        "^if",  // KEYWORD
        "^for",  // KEYWORD
        "^do",  // KEYWORD
        "^while",  // KEYWORD
        "^output",  // KEYWORD
        "^switch",  // KEYWORD
        "^continue",  // KEYWORD
        "^finally",   // KEYWORD
        "^break",  // KEYWORD
        "^default",  // KEYWORD
        "^case",  // KEYWORD
        "^final",  // KEYWORD
        "^void",  // KEYWORD
        "^main",  // KEYWORD
        

        // reserved words
        "^var",  // RESERVED_WORD
        "^let",  // RESERVED_WORD
        "^const",  // RESERVED_WORD

        // literals
        "^\"([^\r\n\"\\\\]*(?:\\\\.[^\n\r\"\\\\]*)*)\"",  // STRING_LITERAL
        "^('\\\\'')|^('([^']|\\\\n|\\\\t)')",  // CHAR_LITERAL
        "^([0-9]+([.][0-9]*)|^([0-9]*[.][0-9]+))",  // FLOAT_LITERAL
        "^[0-9]+",  // INT_LITERAL
        "^(true)|^(false)",  // BOOL_LITERAL

        // relational operators 
        "^[=][=]",  // EQUAL_TO_OP
        "^[!][=]",  // NOT_EQUAL_TO_OP
        "^[>][=]",  // GREATER_THAN_EQUAL_TO_OP
        "^[>]",  // GREATER_THAN_OP 
        "^[<]=",  // LESS_THAN_EQUAL_TO_OP
        "^[<]",  // LESS_THAN_OP

        // logical operators
        "^[!]",  // NOT_OP
        "^[&][&]",  // AND_OP
        "^[|][|]",  // OR_OP
        
        // unary operators
        "^[+][+]",  // INCREMENT
        "^[-][-]",  // DECREMENT

        // assignment operators 
        "^=",  // ASSIGNMENT
        "^[+]=",  // ADDITION_ASSIGNMENT_OP
        "^[-]=",  // SUBTRACTION_ASSIGNMENT_OP
        "^[*][*]=",  // EXPONENTIATION_ASSIGNMENT_OP
        "^[*]=",  // MULTIPLICATION_ASSIGNMENT_OP
        "^[/][/]=",  // FLOOR_DIVISION_ASSIGNMENT_OP
        "^[/]=",  // DIVISION_ASSIGNMENT_OP
        "^[%]=",  // MODULUS_ASSIGNMENT_OP

        // arithmetic operators
        "^[+]",  // ADDITION
        "^-",  // SUBTRACTION
        "^[*][*]",  // EXPONENTIATION
        "^[*]",  // MULTIPLICATION
        "^//",  // FLOOR_DIVISION  
        "^/",  // DIVISION
        "^%",  // MODULUS

        // punctuations
        "^[;]", // SEMICOLON_DEL
        "^[:]",  // COLON_DEL
        "^[(]",  // OPEN_PARENTHESIS_DEL
        "^[)]",  // CLOSE_PARENTHESIS_DEL
        "^[{]",  // OPEN_CURLY_BRACKET_DEL
        "^[}]",  // CLOSE_CURLY_BRACKET_DEL
        "^[?]",  // TERNARY_OP
        "^\\[",  // OPEN_SQUARE_BRACKET_DEL
        "^\\]",  // CLOSE_SQUARE_BRACKET_DEL
        "^[,]",  // COMMA_DEL

        // whitespace
        "^\\s+", // WHITESPACE

        // identifier
        "^[_a-zA-Z]+\\w*",  // IDENTIFIER
    };

    // Token equivalent of regular expression
    public static final Hashtable<String, String> TOKEN = new Hashtable<String, String>() {{
        // data types
        put("^String", "DATATYPE_KEY");
        put("^int", "DATATYPE_KEY");
        put("^char", "DATATYPE_KEY");
        put("^float", "DATATYPE_KEY");
        put("^bool", "DATATYPE_KEY");

        // reserved word (for now)
        put("^double", "RESERVED_WORD");

        // keywords
        put("^input", "KEYWORD");
        put("^in", "KEYWORD");
        put("^not", "KEYWORD");
        put("^try", "KEYWORD");
        put("^except", "KEYWORD");
        put("^return", "KEYWORD");
        put("^elif", "KEYWORD");
        put("^else", "KEYWORD");
        put("^if", "KEYWORD");
        put("^for", "KEYWORD");
        put("^do", "KEYWORD");
        put("^while", "KEYWORD");
        put("^output", "KEYWORD");
        put("^switch", "KEYWORD");
        put("^continue", "KEYWORD");
        put("^finally", "KEYWORD");
        put("^break", "KEYWORD");
        put("^default", "KEYWORD");
        put("^case",  "KEYWORD");
        put("^final", "KEYWORD");
        put("^void", "KEYWORD");
        put("^main", "KEYWORD");

        // constant and reserved_word
        put("constant", "CONSTANT");
        put("reserved", "RESERVED_WORD");

        // reserved words
        put("^var", "RESERVED_WORD");
        put("^let", "RESERVED_WORD");
        put("^const", "RESERVED_WORD");
        
        // literals
        // "^(\"[^\"]*\")"
        put("^\"([^\r\n\"\\\\]*(?:\\\\.[^\n\r\"\\\\]*)*)\"", "STRING_LITERAL");
        put("^('\\\\'')|^('([^']|\\\\n|\\\\t)')", "CHAR_LITERAL");
        put("^[0-9]+", "INT_LITERAL");
        put("^([0-9]+([.][0-9]*)|^([0-9]*[.][0-9]+))", "FLOAT_LITERAL");
        put("^(true)|^(false)", "BOOL_LITERAL");

        // relational operators
        put("^[=][=]", "EQUAL_TO_OP");
        put("^[!][=]", "NOT_EQUAL_TO_OP");
        put("^[>][=]", "GREATER_THAN_EQUAL_TO_OP");
        put("^[>]", "GREATER_THAN_OP");
        put("^[<]=", "LESS_THAN_EQUAL_TO_OP");
        put("^[<]", "LESS_THAN_OP");

        // logical operators
        put("^[!]", "NOT_OP");
        put("^[&][&]", "AND_OP");
        put("^[|][|]", "OR_OP");

        //  unary operators
        put("^[+][+]", "INCREMENT");
        put("^[-][-]", "DECREMENT");

        // assignment operators
        put("^[+]=", "ADDITION_ASSIGNMENT_OP");
        put("^[-]=", "SUBTRACTION_ASSIGNMENT_OP");
        put("^[*][*]=", "EXPONENTIATION_ASSIGNMENT_OP");
        put("^[*]=", "MULTIPLICATION_ASSIGNMENT_OP");
        put("^[/][/]=", "FLOOR_DIVISION_ASSIGNMENT_OP");
        put("^[/]=", "DIVISION_ASSIGNMENT_OP");
        put("^[%]=", "MODULUS_ASSIGNMENT_OP"); 
        put("^=", "ASSIGNMENT_OP");

        // arithmetic operators        
        put("^[+]", "ADDITION_OP");  
        put("^-", "SUBTRACTION_OP");  
        put("^[*][*]", "EXPONENTIATION_OP"); 
        put("^[*]", "MULTIPLICATION_OP");  
        put("^//", "FLOOR_DIVISION_OP");    
        put("^/", "DIVISION_OP");  
        put("^%", "MODULUS_OP");  
        
        // punctuations
        put("^[;]", "SEMICOLON_DEL");
        put("^[:]", "COLON_DEL");
        put("^[(]", "OPEN_PARENTHESIS_DEL");
        put("^[)]", "CLOSE_PARENTHESIS_DEL");
        put("^[{]", "OPEN_CURLY_BRACKET_DEL");
        put("^[}]", "CLOSE_CURLY_BRACKET_DEL");
        put("^[?]", "TERNARY_OP");
        put("^\\[", "OPEN_SQUARE_BRACKET_DEL");
        put("^\\]", "CLOSE_SQUARE_BRACKET_DEL");
        put("^[,]", "COMMA_DEL");

        // whitespace
        put("^\\s+", "WHITESPACE");

        // comments
        put("^#.*", "SINGLE_LINE_COMMENT");
        put("^'''(.|[\r\n]|)*?'''",  "MULTI_LINE_COMMENT");

        // identifier
        put("^[_a-zA-Z]+\\w*", "IDENTIFIER");
    }};

    public static final String[] STRPATTERNLIST = {
        "^\\\\\"",  // DOUBLE_QUOTE_ESC
        "^\\\\n",  // NEWLINE_ESC
        "^\\\\t",  // HORIZONAL_TAB_ESC
        "^((?!(\\\\t|\\\\n|\\\\\")).)*",  // STRING_LITERAL
    };

    public static final Hashtable<String, String> STRTOKENS = new Hashtable<String, String>() {{
        put("^\\\\\"", "DOUBLE_QUOTE_ESC");
        put("^\\\\n", "NEWLINE_ESC");
        put("^\\\\t", "HORIZONAL_TAB_ESC");
        put("^((?!(\\\\t|\\\\n|\\\\\")).)*", "STRING_LITERAL");
        put("open_quote", "OPEN_DOUBLE_QUOTE_DEL");
        put("close_quote", "CLOSE_DOUBLE_QUOTE_DEL");
    }};

    public static final String[] CHARPATTERNLIST = {
        "^\\\\'",  // SINGLE_QUOTE_ESC
        "^\\\\n",  // NEWLINE_ESC
        "^\\\\t",  // HORIZONAL_TAB_ESC
        "^.*",  // CHAR_LITERAL
    };

    public static final Hashtable<String, String> CHARTOKENS = new Hashtable<String, String>() {{
        put("^\\\\'", "SINGLE_QUOTE_ESC");
        put("^\\\\n", "NEWLINE_ESC");
        put("^\\\\t", "HORIZONAL_TAB_ESC");
        put("^.*", "CHAR_LITERAL");
        put("open_quote", "OPEN_SINGLE_QUOTE_DEL");
        put("close_quote", "CLOSE_SINGLE_QUOTE_DEL");
    }};


    public static final String[] SINGLECOMMENTPATTERNLIST = {
        "^.*",  // STRING_LITERAL
    };

    public static final Hashtable<String, String> SINGLECOMMENTTOKENS = new Hashtable<String, String>() {{
        put("comment", "SINGLE_COMMENT_DEL");
        put("^.*", "STRING_LITERAL");
    }};


    public static final String[] MULTICOMMENTPATTERNLIST = {
        "^[^#\r\n].*",  // STRING_LITERAL
        "^[\n\r]*",  // NEWLINE
    };

    public static final Hashtable<String, String> MULTICOMMENTTOKENS = new Hashtable<String, String>() {{
        put("^[^#\r\n].*", "STRING_LITERAL");
        put("^[\n\r]*", "NEWLINE");
        put("open_multi", "OPEN_MULTI_COMMENT_DEL");
        put("close_multi", "CLOSE_MULTI_COMMENT_DEL");
    }};
}


