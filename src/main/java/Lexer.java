/**
 * Angela Fujihara
 * Mini Compiler - Proj 1 --Lexer Class--
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lexer {
    /**
     * Stores line number of input be tokenized
     */
    private int line;
    /**
     * Tracks position within line of input being tokenized
     */
    private int pos;
    /**
     * Tracks position within Lexer constructoe
     */
    private int position;
    /**
     * Stores a char
     */
    private char chr;
    String s;
    /**
     * Hashmap that stores keywords.
     */
    Map<String, TokenType> keywords = new HashMap<>();

    /**
     * Inner subclass for Tokens
     */
    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;

        /**
         * A method to retrieve the type of Token
         *
         * @return - returns the type of the specific Token in question.
         * Data tyoe returned is a TokenType
         */
        public TokenType getTokentype() {
            return tokentype;
        }

        /**
         * Retrieves the value of the specific token in question
         *
         * @return - A String representation of the value of the Token
         */
        public String getValue() {
            return value;
        }

        /**
         * The line number of the current position within the file being tokenized
         *
         * @return - An integer representation of the current line number
         */
        public int getLine() {
            return line;
        }

        /**
         * Retrieves the current position with the current line in the file being tokenized
         *
         * @return - An integer representation of the current position within the current line
         */
        public int getPos() {
            return pos;
        }

        /**
         * The Token constructor
         *
         * @param token - A variable that stores the type of the Token
         * @param value - A String reprsentation of the value of the Token
         * @param line
         * @param pos
         */
        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token;
            this.value = value;
            this.line = line;
            this.pos = pos;
        }

        /**
         * Method that provides String representations of the Class and its sub-Classes.
         *
         * @return - The requested String representation
         */
        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
            switch (this.tokentype) {
                case Integer:
                    result += String.format("  %4s", value);
                    break;
                case Identifier:
                    result += String.format(" %s", value);
                    break;
                case String:
                    result += String.format(" \"%s\"", value);
                    break;
            }
            return result;
        }
    }

    /**
     * Enumerated type to handle the various TokenTypes
     */
    static enum TokenType {
        End_of_input, Op_multiply, Op_divide, Op_mod, Op_add, Op_subtract,
        Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
        Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
        Keyword_else, Keyword_while, Keyword_print, Keyword_putc, LeftParen, RightParen,
        LeftBrace, RightBrace, Semicolon, Comma, Identifier, Integer, String,
    }

    /**
     * Method to provide error details in incorrect method handling wrong TokenType
     *
     * @param line - the line in the input where the error occurred
     * @param pos  - the position within the line of the inout where the error occurred
     * @param msg  - A message describing the error
     */
    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    /**
     * Lexer Constructor
     *
     * @param source - the input to be tokenized
     */
    Lexer(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("print", TokenType.Keyword_print);
        this.keywords.put("putc", TokenType.Keyword_putc);
        this.keywords.put("while", TokenType.Keyword_while);

    }

    /**
     * A follow method to determine whether the character that follows the current
     * char is expected or not
     *
     * @param expect - The character that is expected
     * @param ifyes  - The TokenType to return if 'expect' parameter is True
     * @param ifno   - The TokenType to return if 'expect' parameter is False
     * @param line   - The location of the expected character
     * @param pos    - The position of the expected character
     * @return - A Token
     */
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int) this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }

    /**
     * @param line - The location of the char to be tokenized
     * @param pos  - The position within the line of the char to be tokenized
     * @return - The Token itself
     */
    Token char_lit(int line, int pos) { // handle character literals
        getNextChar();

        if (!Character.isISOControl(chr) && chr != '\'') {
            char c = chr;
            getNextChar();

            // check for closing single quote
            if (chr == '\'') {
                return new Token(TokenType.Integer, String.valueOf(c), line, pos);
            }
        }
        return new Token(TokenType.End_of_input, "", line, pos);
    }

    /**
     * A method that handles tokenizing of String literals
     *
     * @param start - A variable that holds quotes
     * @param line  - The line of the input where the String begins
     * @param pos   - The position within the line of the input where the String begins
     * @return - A Token
     */
    Token string_lit(char start, int line, int pos) { // handle string literals
        StringBuilder result = new StringBuilder();
        // code here
        char nextChar = getNextChar();

        while (nextChar != start) {
            result.append(nextChar);
            nextChar = getNextChar();
        }

        getNextChar();
        return new Token(TokenType.String, result.toString(), line, pos);
    }

    /**
     * A method do determine whether a single / is a division symbol or the beginning of a comment
     *
     * @param line - The line where the first / can be found
     * @param pos  - The position within the line where the first / can be found
     * @return - A Token
     */
    Token div_or_comment(int line, int pos) { // handle division or comments
        char firstChar = getNextChar();
        char secondChar = getNextChar();
        char thirdChar;
        if (firstChar == '*') {
            while (secondChar != firstChar) {
                secondChar = getNextChar();
            }

            thirdChar = getNextChar();
            if (thirdChar == '/') {
                getToken();
            }
        } else if (firstChar == '/') {
            while (secondChar != '\n') {
                secondChar = getNextChar();
            }
            getToken();
        }
        return new Token(TokenType.Op_divide, "/", line, pos);
    }


    /**
     * A method that determines whether input to be tokenized is an integer or identifier. And whether the
     * identifier is a user generated variable or a language keyword
     *
     * @param line - The line within the input where the furst char of the value to be evaluated resides
     * @param pos  - The position within the line
     * @return - A Token
     */
    Token identifier_or_integer(int line, int pos) { // handle identifiers and integers
        boolean is_number = true;
        // code here
        String result = "";

        if (Character.isDigit(chr)) {

            while (Character.isDigit(chr)) {
                result += (chr);
                getNextChar();
            }

            return new Token(TokenType.Integer, result, line, pos);
        } else if (Character.isLetter(chr)) {
            while (Character.isLetter(chr) || Character.isDigit(chr)) {
                result += (chr);
                getNextChar();
            }
            if (keywords.containsKey(result)) {
                return new Token(keywords.get(result), result, line, pos);
            }
        }

        return new Token(TokenType.Identifier, result, line, pos);
    }

    /**
     * A method to retrieve a Token with built-in switch case which determines method responsibility
     * for tokenizing input
     *
     * @return - A Token
     */
    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        // switch statement on character for all forms of tokens with return to follow.... one example left for you
        switch (this.chr) {
            case '\u0000':
                return new Token(TokenType.End_of_input, "", line, pos);
            // remaining case statements
            case '*':
                getNextChar();
                return new Token(TokenType.Op_multiply, "*", line, pos);
            case '/':
                return div_or_comment(line, pos);
            case '%':
                getNextChar();
                return new Token(TokenType.Op_mod, "%", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.Op_add, "+", line, pos);
            case '-':
                char firstChar = getNextChar();
                if (firstChar == ' ') {
                    char secondChar = getNextChar();// char to be subtracted
                    if (secondChar != ' ') {
                        return new Token(TokenType.Op_subtract, "-", line, pos);
                    }
                } else if (firstChar != ' ') {
                    return new Token(TokenType.Op_negate, "-", line, pos);
                }
            case '!':
                if (getNextChar() == '=') {
                    return new Token(TokenType.Op_notequal, "!=", line, pos);
                } else {
                    return new Token(TokenType.Op_not, "!", line, pos);
                }
            case '<':
                if (getNextChar() == '=') {
                    return new Token(TokenType.Op_lessequal, "<=", line, pos);
                } else {
                    return new Token(TokenType.Op_less, "<", line, pos);
                }
            case '>':
                if (getNextChar() == '=') {
                    return new Token(TokenType.Op_greaterequal, ">=", line, pos);
                } else {
                    return new Token(TokenType.Op_greater, ">", line, pos);
                }
            case '=':
                getNextChar();
                TokenType tokenType = follow('=', TokenType.Op_equal, TokenType.Op_assign, line, pos).tokentype;
                if (tokenType == TokenType.Op_equal) {
                    return new Token(tokenType, "==", line, pos);
                } else {
                    return new Token(tokenType, "=", line, pos);
                }
            case '&':
                if (getNextChar() == '&') {
                    return new Token(TokenType.Op_and, "&&", line, pos);
                } else {
                    return new Token(TokenType.Identifier, "&", line, pos);
                }
            case '|':
                if (getNextChar() == '|') {
                    return new Token(TokenType.Op_or, "||", line, pos);
                } else {
                    return new Token(TokenType.Identifier, "|", line, pos);
                }
            case '(':
                getNextChar();
                return new Token(TokenType.LeftParen, "(", line, pos);
            case ')':
                getNextChar();
                return new Token(TokenType.RightParen, ")", line, pos);
            case '{':
                getNextChar();
                return new Token(TokenType.LeftBrace, "{", line, pos);
            case '}':
                getNextChar();
                return new Token(TokenType.RightBrace, "}", line, pos);
            case ';':
                getNextChar();
                return new Token(TokenType.Semicolon, ";", line, pos);
            case ',':
                getNextChar();
                return new Token(TokenType.Comma, ",", line, pos);
            case '"':
                return string_lit('"', line, pos);
            case '\'':
                return char_lit(line, pos);
            default:
                return identifier_or_integer(line, pos);
        }
    }

    /**
     * Retrieves the next char from the input
     *
     * @return - The requested char
     */
    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    /**
     * A method to print requested Tokens
     *
     * @return - A String representation of the printed Tokens
     */
    String printTokens() {
        Token t;
        StringBuilder sb = new StringBuilder();
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            sb.append(t);
            sb.append("\n");
            System.out.println(t);
        }
        sb.append(t);
        System.out.println(t);
        return sb.toString();
    }

    /**
     * A method to output Tokens to a file
     *
     * @param result
     */
    static void outputToFile(String result, String fileName) {

        //this file writes to hello.lex
        if (fileName == "src/main/resources/hello.lex") {
            try {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(result);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //this file writes to hello2.lex
        } else if (fileName == "src/main/resources/hello2.lex") {
            try {
                FileWriter myWriter = new FileWriter("src/main/resources/hello2.lex");
                myWriter.write(result);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (fileName == "src/main/resources/hello3.lex") {
            try {
                FileWriter myWriter = new FileWriter("src/main/resources/hello3.lex");
                myWriter.write(result);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (fileName == "src/main/resources/hello4.lex") {
            try {
                FileWriter myWriter = new FileWriter("src/main/resources/hello4.lex");
                myWriter.write(result);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //this file writes to hello3.lex
            try {
                FileWriter myWriter = new FileWriter("src/main/resources/hello5.lex");
                myWriter.write(result);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * main method
     *
     * @param args - A String array
     */
    public static void main(String[] args) {
        if (1 == 1) {
            try {
                File file1 = new File("src/main/resources/prime.c");
                Scanner scanner1 = new Scanner(file1);
                String source = " ";
                String result = " ";
                while (scanner1.hasNext()) {
                    source += scanner1.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result, "src/main/resources/hello.lex");
                scanner1.close();

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
            try {
                File file2 = new File("src/main/resources/99bottles.c");
                Scanner scanner2 = new Scanner(file2);
                String source = " ";
                String result = " ";
                while (scanner2.hasNext()) {
                    source += scanner2.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result, "src/main/resources/hello2.lex");
                scanner2.close();

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
            try {
                File file3 = new File("src/main/resources/fizzbuzz.c");
                Scanner scanner3 = new Scanner(file3);
                String source = " ";
                String result = " ";
                while (scanner3.hasNext()) {
                    source += scanner3.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result, "src/main/resources/hello3.lex");
                scanner3.close();

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
            try {
                File file3 = new File("src/main/resources/AngelaFileRead1.lex");
                Scanner scanner3 = new Scanner(file3);
                String source = " ";
                String result = " ";
                while (scanner3.hasNext()) {
                    source += scanner3.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result, "src/main/resources/hello4.lex");
                scanner3.close();

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
            try {
                File file3 = new File("src/main/resources/AngelaFileRead2.lex");
                Scanner scanner3 = new Scanner(file3);
                String source = " ";
                String result = " ";
                while (scanner3.hasNext()) {
                    source += scanner3.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result, "src/main/resources/hello5.lex");
                scanner3.close();

            } catch (FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            error(-1, -1, "No args");
        }

    }
}
