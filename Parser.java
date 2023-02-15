import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashSet;
public class Parser {
    static ArrayList<String> lexemes = new ArrayList<>();
    static ArrayList<String> tokens = new ArrayList<>();
    static int pointer = 0;
    static String lexeme;
    static String token; 
    static HashSet<String> declaredIdentifiers = new HashSet<>();
    static HashSet<String> initializedIdentifiers = new HashSet<>();
    static HashSet<String> constants = new HashSet<>();
    static boolean assignmentFlag = false;


    public static void main(String[] args) {
        try {
            // convert .njs file into Java String
            Scanner scanner = new Scanner(new File(args[0]));

            String fileExtension = args[0].substring(args[0].indexOf('.'));

            // throw Exception if file extension is not ".njs"
            if (!fileExtension.equals(".nlex")) {
                throw new Exception("Input File only accepts \".nlex\" extension!");
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int delIndex = line.lastIndexOf(",");
                String[] lexTokenSplit = {line.substring(0, delIndex), line.substring(delIndex+1)}; 
                lexemes.add(lexTokenSplit[0]);
                tokens.add(lexTokenSplit[1]);
            }           
            
            if (lexemes.size() > 0 && tokens.size() > 0) {
                // perform parsing
                lexeme = getLexeme();
                token = getToken();
                if (program() && lexemes.isEmpty() && tokens.isEmpty()) {
                    System.out.println("Syntax Correct");
                } else {
                    System.out.printf("Syntax Error");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean program() {
        if (lexemeCheck("void")) {
            if (lexemeCheck("main")) {
                if (lexemeCheck("(")) {
                    if (lexemeCheck(")")) {
                        if (mainBody()) {
                            if (tokens.isEmpty() && lexemes.isEmpty()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean mainBody() {
        if (block()) {
            return true;
        } 
        return false;
    }

    public static boolean lexemeCheck(String s) {
        if (lexeme.equals(s)) {
            consume();
            return true;
        } else {
            return false;
        }
    }


    public static boolean tokenCheck(String s) {
        if (token.equals(s)) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    // <block> ::= { <block statements>? }
    public static boolean block() {
        int tokenSize;
        boolean flag;
        if (tokenCheck("OPEN_CURLY_BRACKET_DEL")) {
            tokenSize = tokens.size();
            flag = blockStatements();
            if (tokens.size() != tokenSize && flag == false) {
                return false;
            }
            if (tokenCheck("CLOSE_CURLY_BRACKET_DEL")) {
                return true;
            }            
        }
        return false;
    }      

    // <block statements> ::= <block statement> | <block statement> <block statements>
    public static boolean blockStatements() {
        if (blockStatement()) {
            int tokenSize = tokens.size();
            while (blockStatement()) {
                tokenSize = tokens.size();
            }
            if (tokenSize == tokens.size()) {
                return true;
            } else if (tokenSize != tokens.size()){
                return false;
            }
            return true;
        }        
        return false;
    }       

    // <block statement> ::= <final declaration> | <local variable declaration statement> | <statement>
    public static boolean blockStatement() {
        int tokenSize = tokens.size();

        if (tokenSize == tokens.size() && finalDeclaration()) {
            return true;
        } else if (tokenSize == tokens.size() && localVariableDeclarationStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && statement()) {
            return true;
        }

        return false;
    }      

    // <local variable declaration statement> ::= <local variable declaration> ;
    public static boolean localVariableDeclarationStatement() {
        if (localVariableDeclaration()) {
            if (tokenCheck("SEMICOLON_DEL")) {
                return true;
            }
        }
        return false;
    }      

    // <statement> ::= <statement without trailing substatement> | <if then statement> | <if then else statement> | <while statement> | <for statement> 
    public static boolean statement() {
        int tokenSize = tokens.size();
        /* 
        ArrayList<String> lexemesCopy = new ArrayList<>(lexemes);
        ArrayList<String> tokensCopy = new ArrayList<>(tokens);

        if (statementWithoutTrailingStatement()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (ifThenElseStatement()) {
            return true;
        }
        
        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (ifThenStatement()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (whileStatement()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (forStatement()) {
            return true;
        }
        */
        if (tokenSize == tokens.size() && statementWithoutTrailingStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && ifStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && whileStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && forStatement()) {
            return true;
        }
        return false;
    }     

    // <statement no short if> ::= <statement without trailing substatement> |  <if then else statement no short if> | <while statement no short if> | <for statement no short if>
    public static boolean statementNoShortIf() {
        ArrayList<String> lexemesCopy = new ArrayList<>(lexemes);
        ArrayList<String> tokensCopy = new ArrayList<>(tokens);

        if (statementWithoutTrailingStatement()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (ifThenElseStatementNoShortIf()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (whileStatementNoShortIf()) {
            return true;
        }

        lexemes = new ArrayList<>(lexemesCopy);
        tokens = new ArrayList<>(tokensCopy);
        if (forStatementNoShortIf()) {
            return true;
        }

        return false;
    }       

    // <statement without trailing substatement> ::= <block> | <empty statement> | <expression statement> | <switch statement> | <do statement> | <break statement> | <continue statement> | <output method invocation> | <do for statement>
    public static boolean statementWithoutTrailingStatement() {
        int tokenSize = tokens.size();

        if (tokenSize == tokens.size() && block()) {
            return true;
        } else if (tokenSize == tokens.size() && emptyStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && expressionStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && switchStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && doStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && breakStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && continueStatement()) {
            return true;
        } else if (tokenSize == tokens.size() && outputMethodInvocation()) {
            return true;
        } 
        return false;
    }    

    // <empty statement> ::= ;
    public static boolean emptyStatement() {
        if (tokenCheck("SEMICOLON_DEL")) {
            return true;
        }
        return false;
    }

    // <expression statement> ::= <statement expression> ;
    public static boolean expressionStatement() {
        if (statementExpression()) {
            if (tokenCheck("SEMICOLON_DEL")) {
                return true;
            }
        }
        return false;
    } 

    // <if statement> ::= if (<expression>) <statement> <elif statements>? <else statement>?
    public static boolean ifStatement() {
        int tokenSize;
        boolean flag;
        if (lexemeCheck("if")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statement()) {
                            tokenSize = tokens.size();
                            flag = elifStatements();
                            if (tokens.size() != tokenSize && flag == false) {
                                return false;
                            }                           

                            tokenSize = tokens.size();
                            flag = elseStatement();
                            if (tokens.size() != tokenSize && flag == false) {
                                return false;
                            }
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }  


    // <elif statements> ::= <elif statement> | <elif statement> <elif statements>
    public static boolean elifStatements() {
        if (elifStatement()) {
            int tokenSize = tokens.size();
            while (elifStatement()) {
                tokenSize = tokens.size();
            }
            if (tokenSize == tokens.size()) {
                return true;
            } else if (tokenSize != tokens.size()){
                return false;
            }
            return true;
        }        
        return false;
    }

    // <elif statement> ::= elif (<expression>) <statement> 
    public static boolean elifStatement() {
        if (lexemeCheck("elif")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statement()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    // <else statement> ::= else <statement>
    public static boolean elseStatement() {
        if (lexemeCheck("else")) {
            if (statement()) {
                return true;
            }
        }
        return false;
    }      
    
    
    // <if then statement>::= if ( <expression> ) <statement>
    public static boolean ifThenStatement() {
        if (lexemeCheck("if")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statement()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }      

    // <if then else statement>::= if ( <expression> ) <statement no short if> else <statement>
    public static boolean ifThenElseStatement() {
        if (lexemeCheck("if")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statementNoShortIf()) {
                            if (lexemeCheck("else")) {
                                if (statement()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }       

    // <if then else statement no short if> ::= if ( <expression> ) <statement no short if> else <statement no short if>
    public static boolean ifThenElseStatementNoShortIf() {
        if (lexemeCheck("if")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statementNoShortIf()) {
                            if (lexemeCheck("else")) {
                                if (statementNoShortIf()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }    

    // <switch statement> ::= switch ( <literal> ) <switch block>
    public static boolean switchStatement() {
        if (lexemeCheck("switch")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (literal()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (switchBlock()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // <switch block> ::= { <switch block statement groups>? <switch labels>? }
    public static boolean switchBlock() {
        int tokenSize;
        boolean flag;
        if (tokenCheck("OPEN_CURLY_BRACKET_DEL")) {
            tokenSize = tokens.size();
            flag = switchBlockStatementGroups();
            if (tokens.size() != tokenSize && flag == false) {
                return false;
            }

            tokenSize = tokens.size();
            flag = switchLabels();
            if (tokens.size() != tokenSize && flag == false) {
                return false;
            }

            if (tokenCheck("CLOSE_CURLY_BRACKET_DEL")) {
                return true;
            }
        }
        return false;
    }

    // <switch block statement groups> ::= <switch block statement group> | <switch block statement group> <switch block statement groups>
    public static boolean switchBlockStatementGroups() {
        if (switchBlockStatementGroup()) {
            int tokenSize = tokens.size();
            while (switchBlockStatementGroup()) {
                tokenSize = tokens.size();
            }
            if (tokenSize == tokens.size()) {
                return true;
            } else if (tokenSize != tokens.size()){
                return false;
            }
            return true;
        }
        return false;
    }

    // <switch block statement group> ::= <switch labels> <block statements>
    public static boolean switchBlockStatementGroup() {
        if (switchLabels()) {
            if (blockStatements()) {
                return true;
            }
        }
        return false;
    }      

    // <switch labels> ::= <switch label> | <switch label> <switch labels> 
    public static boolean switchLabels() {
        if (switchLabel()) {
            int tokenSize = tokens.size();
            while (switchLabel()) {
                tokenSize = tokens.size();
            }
            if (tokenSize == tokens.size()) {
                return true;
            } else if (tokenSize != tokens.size()){
                return false;
            }
            return true;
        }
        return false;
    }    

    // <switch label> ::= case <literal> : | default :
    public static boolean switchLabel() {
        if (lexemeCheck("case")) {
            if (literal()) {
                if (tokenCheck("COLON_DEL")) {
                    return true;
                }
            }
        } else if (lexemeCheck("default")) {
            if (tokenCheck("COLON_DEL")) {
                return true;
            }
        }
        return false;
    }

    // <while statement> ::= while ( <expression> ) <statement>
    public static boolean whileStatement() {
        if (lexemeCheck("while")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statement()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }   

    // <while statement no short if> ::= while ( <expression> ) <statement no short if>
    public static boolean whileStatementNoShortIf() {
        if (lexemeCheck("while")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                if (expression()) {
                    if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                        if (statementNoShortIf()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // <do statement> ::= do <statement> while ( <expression> ) ; | do <statement> <for no body> ;
    public static boolean doStatement() {
        //ArrayList<String> lexemesCopy = new ArrayList<>(lexemes);
        //ArrayList<String> tokensCopy = new ArrayList<>(tokens);
        if (lexemeCheck("do")) {
            if (statement()) {
                if (lexeme.equals("for")) {
                    if (forNoBody()) {
                        if (tokenCheck("SEMICOLON_DEL")) {
                            return true;
                        }                    }
                } else if (lexeme.equals("while")) {
                    if (lexemeCheck("while")) {
                        if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                            if (expression()) {
                                if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                                    if (tokenCheck("SEMICOLON_DEL")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } 
        return false;
    }


    // <do for statement> ::= do <statement> <for no body> ;
    public static boolean doForStatement() {
        if (lexemeCheck("do")) {
            if (statement()) {
                if (forNoBody()) {
                    if (tokenCheck("SEMICOLON_DEL")) {
                        return true;
                    }
                }
            }
        }
        return false;
    } 

    // <for no body> ::=  ;
    public static boolean forNoBody() {
        int tokenSize;
        boolean flag;
        if (lexemeCheck("for")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                tokenSize = tokens.size();
                flag = forInit();
                if (tokens.size() != tokenSize && flag == false) {
                    return false;
                }
                if (tokenCheck("SEMICOLON_DEL")) {
                    tokenSize = tokens.size();
                    flag = expression();
                    if (tokens.size() != tokenSize && flag == false) {
                        return false;
                    }
                    if (tokenCheck("SEMICOLON_DEL")) {
                        tokenSize = tokens.size();
                        flag = forUpdate();
                        if (tokens.size() != tokenSize && flag == false) {
                            return false;
                        }
                        if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    } 

    

    // <for statement no short if> ::= for ( <for init>? ; <expression>? ; <for update>? ) <statement>
    public static boolean forStatement() {
        int tokenSize;
        boolean flag;
        if (lexemeCheck("for")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                tokenSize = tokens.size();
                flag = forInit();
                if (tokens.size() != tokenSize && flag == false) {
                    return false;
                }
                if (tokenCheck("SEMICOLON_DEL")) {
                    tokenSize = tokens.size();
                    flag = expression();
                    if (tokens.size() != tokenSize && flag == false) {
                        return false;
                    }
                    if (tokenCheck("SEMICOLON_DEL")) {
                        tokenSize = tokens.size();
                        flag = forUpdate();
                        if (tokens.size() != tokenSize && flag == false) {
                            return false;
                        }
                        if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                            if (statement()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // <for statement no short if> ::= for ( <for init>? ; <expression>? ; <for update>? ) <statement no short if>
    public static boolean forStatementNoShortIf() {
        int tokenSize;
        boolean flag;
        if (lexemeCheck("for")) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                tokenSize = tokens.size();
                flag = forInit();
                if (tokens.size() != tokenSize && flag == false) {
                    return false;
                }
                if (tokenCheck("SEMICOLON_DEL")) {
                    tokenSize = tokens.size();
                    flag = expression();
                    if (tokens.size() != tokenSize && flag == false) {
                        return false;
                    }
                    if (tokenCheck("SEMICOLON_DEL")) {
                        tokenSize = tokens.size();
                        flag = forUpdate();
                        if (tokens.size() != tokenSize && flag == false) {
                            return false;
                        }
                        if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                            if (statementNoShortIf()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // <for init> ::= <statement expression list> | <local variable declaration>
    public static boolean forInit() {
        if (statementExpressionList()) {
            return true;
        } else if (localVariableDeclaration()) {
            return true;
        }
        return false;
    } 

    // <local variable declaration> ::= <type> <variable declarators>
    public static boolean localVariableDeclaration() {
        if (type()) {
            if (variableDeclarators()) {
                return true;
            }
        }
        return false;
    }

    // <for update> ::= <statement expression list>
    public static boolean forUpdate() { 
        if (statementExpressionList()) {
            return true;
        }
        return false;
    }

    // <statement expression> ::= <assignment> | <preincrement expression> | <postincrement expression> | <predecrement expression> | <postdecrement expression>
    public static boolean statementExpression() { 
        if (tokens.size() > 1) {            
            if (assignment()) {
                return true;
            } else if (preIncrementExpression()) {
                return true;
            } else if (preDecrementExpression()) {
                return true;
            } else if (postIncrementExpression()) {
                return true;
            } else if (postDecrementExpression()) {
                return true;
            }
        }
        return false;
    }


    // <statement expression list> ::= <statement expression> | <statement expression> ,  <statement expression list>
    public static boolean statementExpressionList() { 
        if (statementExpression()) {
            if (tokenCheck("COMMA_DEL")) {
                if (statementExpressionList()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // <break statement> ::= break;
    public static boolean breakStatement() { 
        if (lexemeCheck("break")) {
            if (tokenCheck("SEMICOLON_DEL")) {
                return true;
            }
        }
        return false;
    }

    // <continue statement> ::= continue;
    public static boolean continueStatement() {
        if (lexemeCheck("continue")) {
            if (tokenCheck("SEMICOLON_DEL")) {
                return true;
            }
        }
        return false;
    }

    // <output method invocation> ::= <output method invocation> ( <argument list>? );
    public static boolean outputMethodInvocation() {
        int tokenSize;
        boolean flag;
        if (outputMethodName()) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                tokenSize = tokens.size();
                flag = argumentList();
                if (tokens.size() != tokenSize && flag == false) {
                    return false;
                }
                if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                    if (tokenCheck("SEMICOLON_DEL")) {
                        return true;
                    }
                }
            }
        } 
        return false;
    }

    // <input method invocation> ::= <input method invocation> ( <string literal>? )
    public static boolean inputMethodInvocation() {
        if (inputMethodName()) {
            if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
                stringLiteral();
                if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                    return true;
                }
            }
        }
        return false;
    }

    // <argument list> ::= <expression> | <expression> , <argument list>
    public static boolean argumentList() {
        if (expression()) {
            if (tokenCheck("COMMA_DEL")) {
                if (argumentList()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // <input name> ::= input
    public static boolean inputMethodName() {
        if (lexemeCheck("input")) {
            return true;
        } 
        return false;
    }

    // <output method name> ::= output
    public static boolean outputMethodName() {
        if (lexemeCheck("output")) {
            return true;
        } 
        return false;
    }

    // <expression for> ::= <for no body> : expression
    public static boolean expressionFor() {
        if (forNoBody()) {
            if (tokenCheck("COLON_DEL")) {
                if (expression()) {
                    return true;
                }
            }
        }
        return false;
    }

    // <constant expression> ::= <expression>
    public static boolean constantExpression() {
        if (expression()) {
            return true;
        }
        return false;
    }

    // <expression> ::= <assignment expression>
    public static boolean expression() {
        if (assignmentExpression()) {
            return true;
        }
        return false;
    }

    // <assignment expression> ::= <conditional expression> | <assignment>
    public static boolean assignmentExpression() {
        String[] assignOps = {"ASSIGNMENT_OP", "ADDITION_ASSIGNMENT_OP", "SUBTRACTION_ASSIGNMENT_OP", "MULTIPLICATION_ASSIGNMENT_OP",
        "DIVISION_ASSIGNMENT_OP", "MODULUS_ASSIGNMENT_OP", "EXPONENTIATION_ASSIGNMENT_OP", "FLOOR_DIVISION_ASSIGNMENT_OP"};
        for (String op: assignOps) {
            if (tokens.get(0).equals("IDENTIFIER") && tokens.get(1).equals(op)) {
                if (assignment()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (conditionalExpression()) {
            return true;
        }
        return false;
    }

    // <assignment> ::= <left hand side> <assignment operator> <assignment expression>
    public static boolean assignment() {
        assignmentFlag = true;
        String identifier = lexeme;
        if (tokens.size() > 1) {
            String[] assignOps = {"ASSIGNMENT_OP", "ADDITION_ASSIGNMENT_OP", "SUBTRACTION_ASSIGNMENT_OP", "MULTIPLICATION_ASSIGNMENT_OP",
            "DIVISION_ASSIGNMENT_OP", "MODULUS_ASSIGNMENT_OP", "EXPONENTIATION_ASSIGNMENT_OP", "FLOOR_DIVISION_ASSIGNMENT_OP"};
            if (tokens.get(0).equals("IDENTIFIER")) {
                for (String op: assignOps) {
                    if (tokens.get(1).equals(op)) {
                        if (leftHandSide()) {
                            if (assignmentOperator()) {
                                if (assignmentExpression()) {
                                    // if identifier in declaredIdentifiers
                                    if (declaredIdentifiers.contains(identifier)) {
                                        declaredIdentifiers.remove(identifier);
                                        initializedIdentifiers.add(identifier);
                                    }

                                    // if identifier in constant
                                    if (constants.contains(identifier)) {
                                        assignmentFlag = false;
                                        return false;
                                    }

                                    // if identifier not in initializedIdentifiers
                                    if (!initializedIdentifiers.contains(identifier)) {
                                        assignmentFlag = false;
                                        return false;
                                    }
                                    assignmentFlag = false;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        assignmentFlag = false;
        return false;
    }

    // <left hand side> ::= <expression name>
    public static boolean leftHandSide() {
        if (expressionName()) {
            return true;
        }
        return false;
    }

    // <assignment operator> ::= = | += | -= | *= | /= | %= | **= | //=
    public static boolean assignmentOperator() { 
        String[] assignOps = {"ASSIGNMENT_OP", "ADDITION_ASSIGNMENT_OP", "SUBTRACTION_ASSIGNMENT_OP", "MULTIPLICATION_ASSIGNMENT_OP",
        "DIVISION_ASSIGNMENT_OP", "MODULUS_ASSIGNMENT_OP", "EXPONENTIATION_ASSIGNMENT_OP", "FLOOR_DIVISION_ASSIGNMENT_OP"};
        for (String op: assignOps) {
            if (tokenCheck(op)) {
                return true;
            }
        }
        return false;
    }

    // <conditional expression> ::= <conditional or expression> | <conditional or expression> ? <expression> : <conditional expression>
    public static boolean conditionalExpression() { 
        if (conditionalOrExpression()) {
            if (tokenCheck("TERNARY_OP")) {
                if (expression()) {
                    if (tokenCheck("COLON_DEL")) {
                        if (conditionalExpression()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // <conditional or expression> ::= <conditional and expression> || <conditional or expression> || <conditional and expression>
    public static boolean conditionalOrExpression() { 
        if (conditionalAndExpression()) {
            if (tokenCheck("OR_OP")) {
                if (conditionalOrExpression()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    // <conditional and expression> ::= <and expression> && <conditional and expression> | <and expression>
    public static boolean conditionalAndExpression() { 
        if (andExpression()) {
            if (tokenCheck("AND_OP")) {
                if (conditionalAndExpression()) {
                    return true;
                } else { 
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // <and expression> ::= <equality expression>
    public static boolean andExpression() {
        if (equalityExpression()) {
            return true;
        }
        return false;
    }

    // <equality expression> ::= <relational expression> == <relational expression> | <relational expression> != <relational expression> | <relational expression>
    public static boolean equalityExpression() {
        if (relationalExpression()) {
            if (token.equals("EQUAL_TO_OP") || token.equals("NOT_EQUAL_TO_OP")) {
                if (tokenCheck(token)) {
                    if (relationalExpression()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    // <relational expression> ::= <shift expression> < <relational expression> | <shift expression> > <relational expression> | <shift expression> <= <relational expression> | <shift expression> >= <relational expression> | <shift expression>
    public static boolean relationalExpression() {
        if (shiftExpression()) {
            if (token.equals("GREATER_THAN_OP") || token.equals("GREATER_THAN_EQUAL_TO_OP") ||
            token.equals("LESS_THAN_OP") || token.equals("LESS_THAN_EQUAL_TO_OP")) {
                if (tokenCheck(token)) {
                    if (shiftExpression()) {
                        return true;
                    } else {
                        return false;
                    }
                } 
            }
            return true;
        }
        return false; 
    }

    // <shift expression> ::= <additive expression>
    public static boolean shiftExpression() { 
        if (additiveExpression()) {
            return true;
        }
        return false;
    }

    // <additive expression> ::= <multiplicative expression> + <additive expression> | <multiplicative expression> - <additive expression> | <multiplicative expression>
    public static boolean additiveExpression() { 
        if (multiplicativeExpression()) {
            if (token.equals("ADDITION_OP") || token.equals("SUBTRACTION_OP")) {
                if (tokenCheck(token)) {
                    if (additiveExpression()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    // <multiplicative expression> ::= <unary expression> * <multiplicative expression> |  <unary expression> / <multiplicative expression>  |  <unary expression> % <multiplicative expression>  |  <unary expression> ** <multiplicative expression> |  <unary expression> // <multiplicative expression> | <unary expression> 
    public static boolean multiplicativeExpression() { 
        if (unaryExpression()) {
            if (token.equals("MULTIPLICATION_OP") || token.equals("EXPONENTIATION_OP") || 
            token.equals("FLOOR_DIVISION_OP") || token.equals("DIVISION_OP") || token.equals("MODULUS_OP")) {
                if (tokenCheck(token)) {
                    if (multiplicativeExpression()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } 
            return true;
        }
        return false;
    }

    // <unary expression> ::= <preincrement expression> | <predecrement expression> | + <unary expression> | - <unary expression> | <unary expression not plus minus>
    public static boolean unaryExpression() { 
        if (preIncrementExpression()) {
            return true;
        } else if (preDecrementExpression()) {
            return true;
        } else if (tokenCheck("ADDITION_OP")) {
            if (unaryExpression()) {
                return true;
            }
        } else if (tokenCheck("SUBTRACTION_OP")) {
            if (unaryExpression()) {
                return true;
            }
        } else if (unaryExpressionNotPlusMinus()) {
            return true;
        }
        return false;
    }

    // <predecrement expression> ::= -- <unary expression>
    public static boolean preDecrementExpression() { 
        if (tokens.size() > 1 && tokens.get(0).equals("DECREMENT") && tokens.get(1).equals("IDENTIFIER")) {
            String identifier = tokens.get(1);

            // if identifier in constant
            if (constants.contains(identifier)) {
                return false;
            }
            
            // if identifier is in declaredIdentifiers
            if (declaredIdentifiers.contains(identifier)) {
                return false;
            }            

            if (tokenCheck("DECREMENT")) {
                if (expressionName()) {
                    return true;
                }
            }
        }
        return false;
    }

    // <preincrement expression> ::= ++ <unary expression>
    public static boolean preIncrementExpression() { 
        if (tokens.size() > 1 && tokens.get(0).equals("INCREMENT") && tokens.get(1).equals("IDENTIFIER")) {
            String identifier = tokens.get(1);

            // if identifier in constant
            if (constants.contains(identifier)) {
                return false;
            }
            
            // if identifier is in declaredIdentifiers
            if (declaredIdentifiers.contains(identifier)) {
                return false;
            } 

            if (tokenCheck("INCREMENT")) {
                if (expressionName()) {
                    return true;
                }
            }
        }
        return false;
    }

    // <unary expression not plus minus> ::= ! <unary expression> | <postfix expression>
    public static boolean unaryExpressionNotPlusMinus() {
        if (tokenCheck("NOT_OP")) {
            if (unaryExpression()) {
                return true;
            }
        } else if (postfixExpression()) {
            return true;
        }
        return false;
    }

    // <postdecrement expression> ::= <postfix expression> --
    public static boolean postDecrementExpression() {
        if (tokens.size() > 1 && tokens.get(0).equals("IDENTIFIER") && tokens.get(1).equals("DECREMENT")) {
            if (expressionName()) {
                if (tokenCheck("DECREMENT")) {
                    return true;
                }
            }
        }
        return false;
    }

    // <postincrement expression> ::= <postfix expression> ++
    public static boolean postIncrementExpression() {
        if (tokens.size() > 1 && tokens.get(0).equals("IDENTIFIER") && tokens.get(1).equals("INCREMENT")) {
            if (expressionName()) {
                if (tokenCheck("INCREMENT")) {
                    return true;
                }
            }
        }
        return false;
    }

    // <postfix expression> ::= <primary> | <postincrement expression> | <postdecrement expression> | <expression name> 
    public static boolean postfixExpression() {
        String identifier = lexeme;
        // <primary>
        if (primary()) {
            return true;
        } else if (tokens.size() > 1 && tokens.get(0).equals("IDENTIFIER") && tokens.get(1).equals("INCREMENT")) {
            // if identifier in constant
            if (constants.contains(identifier)) {
                return false;
            }

            // if identifier is in declaredIdentifiers
            if (declaredIdentifiers.contains(identifier)) {
                return false;
            }

            postIncrementExpression();
            return true;
        } else if (tokens.size() > 1 && tokens.get(0).equals("IDENTIFIER") && tokens.get(1).equals("DECREMENT")) {
            // if identifier in constant
            if (constants.contains(identifier)) {
                return false;
            }
            
            // if identifier is in declaredIdentifiers
            if (declaredIdentifiers.contains(identifier)) {
                return false;
            }

            postDecrementExpression();
            return true;
        } else if (expressionName()) {
            return true;
        }
        // <expression name>
        return false;
    }

    // <primary> ::= <primary no new array>
    public static boolean primary() {
        if (primaryNoNewArray()) {
            return true;
        }
        return false;
    }

    // <primary no new array> ::= <literal> | ( <expression> ) |  <input method invocation> | <expression for>
    public static boolean primaryNoNewArray() {
        // literal 
        if (literal()) {
            return true;
        // (expression)
        } else if (tokenCheck("OPEN_PARENTHESIS_DEL")) {
            if (expression()) {
                if (tokenCheck("CLOSE_PARENTHESIS_DEL")) {
                    return true;
                }
            }
        } else if (inputMethodInvocation()) {
            return true;
        } else if (expressionFor()) {
            return true;
        }
        return false;
    }

    // <final declaration> ::= <final modifier> <type> <final declarators>;
    public static boolean finalDeclaration() {
        if (finalModifier()) {
            if (type()) {
                if (finalDeclarators()) {
                    if (tokenCheck("SEMICOLON_DEL")) {
                        return true;
                    }
                } 
            }
        }
        return false;
    }

    // lexemeCheck("final")
    //<final modifier> ::= final
    public static boolean finalModifier() {
        if (lexemeCheck("final")) {
            return true;
        }
        return false;
    }

    //<final declarators> ::= <final declarator> | <final declarator>, <final declarators>
    public static boolean finalDeclarators() {
        if (finalDeclarator()) {
            if (tokenCheck("COMMA_DEL")) {
                if(finalDeclarators()) {
                    return true;
                } else {
                    return false;
                }
            } 
            return true;
        }
        return false;
    }    

    // <final declarator> ::= <final declarator id> = <final initializer>    
    public static boolean finalDeclarator() {
        String identifier = lexeme;
        if (finalDeclaratorId()) {
            if (tokenCheck("ASSIGNMENT_OP")) {
                if (finalInitializer()) {
                    // if identifier in constants or declaredIdentifiers of initializedIdentifiers
                    if (constants.contains(identifier) || declaredIdentifiers.contains(identifier) || initializedIdentifiers.contains(identifier)) {
                        return false;
                    }

                    constants.add(identifier);
                    return true;
                } 
            } 
        }
        return false;
    }

    // <final declarator id> ::= <identifier>
    public static boolean finalDeclaratorId() {
        if (tokenCheck("IDENTIFIER")) {
            return true;
        }
        return false;
    }    

    // <final initializer> ::= <expression> 
    public static boolean finalInitializer() {
        if (expression()) {
            return true;
        }
        return false;
    }

    // <type> ::= int | float | char | boolean | String
    public static boolean type() {
        String [] types = {"int", "float", "char", "bool", "String"};
        for (String t : types) {
            if (lexemeCheck(t)) {
                return true;
            }
        }
        return false;
    }



    // <variable declarators> ::= <variable declarator> | <variable declarator>, <variable declarators> 
    public static boolean variableDeclarators() {
        if (variableDeclarator()) {
            if (tokenCheck("COMMA_DEL")) {
                if(variableDeclarators()) {
                    return true;
                } else {
                    return false;
                }
            } 
            return true;
        }
        return false;
    }

    // <variable declarator> ::= <variable declarator id> | <variable declarator id> = <variable initializer>
    public static boolean variableDeclarator() {
        String identifier = lexeme;
        if (variableDeclaratorId()) {
            if (tokenCheck("ASSIGNMENT_OP")) {
                if (variableInitializer()) {
                    // if identifier in constants or declaredIdentifiers of initializedIdentifiers
                    if (constants.contains(identifier) || declaredIdentifiers.contains(identifier) || initializedIdentifiers.contains(identifier)) {
                        return false;
                    }

                    initializedIdentifiers.add(identifier);
                    return true;
                } 
                return false;
            }
            // if identifier in constants or declaredIdentifiers of initializedIdentifiers
            if (constants.contains(identifier) || declaredIdentifiers.contains(identifier) || initializedIdentifiers.contains(identifier)) {
                return false;
            }            
            declaredIdentifiers.add(identifier); 
            return true;
        }
        return false;
    }

    // <variable declarator id> ::= <identifier>
    public static boolean variableDeclaratorId() {
        if (tokenCheck("IDENTIFIER")) {
            return true;
        }
        return false;
    }

    // <variable initializer> ::= <expression> | <variable declarator id>
    public static boolean variableInitializer() {
        if (expression()) {
            return true;
        }
        return false;
    }

    // <string literal> ::= "<stringStream>"
    public static boolean stringLiteral() {
        if (tokenCheck("OPEN_DOUBLE_QUOTE_DEL")) {
            stringStream();
            if (tokenCheck("CLOSE_DOUBLE_QUOTE_DEL")) {
                return true;
            }
        }
        return false;
    }

    /*
     <string stream> := STRING_LITERAL <string stream> | NEWLINE_ESC <string stream> | 
     HORIZONAL_TAB_ESC <string stream> | DOUBLE_QUOTE_ESC <string stream> | epsilon
     */
    public static boolean stringStream() {
        boolean flag = false;
        String[] streams = {"STRING_LITERAL", "NEWLINE_ESC", "HORIZONAL_TAB_ESC", "DOUBLE_QUOTE_ESC"};
        for (String s: streams) {
            if (tokenCheck(s)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            stringStream();
        }
        return true; 
    }


    // <expression name> ::= <identifier>
    public static boolean expressionName() {
        String identifier = lexeme;
        if (tokenCheck("IDENTIFIER")) {

            // identifier not found in constants, declaredIdentifiers, initializedIdentifiers
            if (!constants.contains(identifier) && !declaredIdentifiers.contains(identifier) && !initializedIdentifiers.contains(identifier)) {
                return false;
            }

            // if assignment flag is false and identifier not in declared identifiers
            if (assignmentFlag == false && declaredIdentifiers.contains(identifier)) {
                return false;
            }
            return true;
        }
        return false;
    }

    // <expression> :: INT_LITERAL | FLOAT_LITERAL | CHAR_LITERAL | BOOL_LITERAL | STRING_LITERAL
    public static boolean literal() {
        String [] literals = {"INT_LITERAL", "FLOAT_LITERAL", "BOOL_LITERAL"};
        for (String literal : literals) {
            if (tokenCheck(literal)) {
                return true;
            }
        }

        if (stringLiteral()) {
            return true;
        } else if (charLiteral()) {
            return true;
        } else if (expressionName()) {
            return true;
        }
            return false;
        }

    /*
     <char literal> := SINGLE_QUOTE_ESC | NEWLINE_ESC | HORIZONAL_TAB_ESC | CHAR_LITERAL
     */
    public static boolean charLiteral() {
        String[] streams = {"SINGLE_QUOTE_ESC", "NEWLINE_ESC", "HORIZONAL_TAB_ESC", "CHAR_LITERAL"};
        if (tokenCheck("OPEN_SINGLE_QUOTE_DEL")) {
            for (String s: streams) {
                if (tokenCheck(s)) {
                    if (tokenCheck("CLOSE_SINGLE_QUOTE_DEL")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static String getLexeme() {
        return lexemes.get(0);
    }

    public static String getToken() {
       return tokens.get(0);
    }

    public static void consume() {
        lexemes.remove(0);
        tokens.remove(0);
        if (lexemes.isEmpty() && tokens.isEmpty()) {
            lexeme = "";
            token = "";
        } else {
            lexeme = getLexeme();
            token= getToken();
        }
    }
}
