import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lexer {
    private int line;
    private int pos;
    private int position;
    private char chr;
    String s;

    Map<String, TokenType> keywords = new HashMap<>();

    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;

        public TokenType getTokentype() {
            return tokentype;
        }

        public String getValue() {
            return value;
        }

        public int getLine() {
            return line;
        }

        public int getPos() {
            return pos;
        }

        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token; this.value = value; this.line = line; this.pos = pos;
        }
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

    static enum TokenType {
        End_of_input, Op_multiply,  Op_divide, Op_mod, Op_add, Op_subtract,
        Op_negate, Op_not, Op_less, Op_lessequal, Op_greater, Op_greaterequal,
        Op_equal, Op_notequal, Op_assign, Op_and, Op_or, Keyword_if,
        Keyword_else, Keyword_while, Keyword_print, Keyword_putc, LeftParen, RightParen,
        LeftBrace, RightBrace, Semicolon, Comma, Identifier, Integer, String
    }

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

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
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int)this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }
    //TODO: Do more research regarding second return
    Token char_lit(int line, int pos) { // handle character literals
        getNextChar();

        // Check if the next character is a valid character literal (any character except quote)
        if (!Character.isISOControl(chr) && chr != '\'') {
            char c = chr; // Capture the character
            getNextChar(); // Skip the character itself

            // Check for closing quote
            if (chr == '\'') {
                return new Token(TokenType.Integer, String.valueOf(c), line, pos);
            } else {
                error(line, pos, "Invalid char literal");
                // Handle error: unmatched closing quote, could return an error token here
            }
        }

//        } else {
//            error(line, pos, "Invalid character, possibly ISO character");
//            // Handle error: invalid character (control character or quote), could return an error token here
//        }




        // If we reach here, it's an error condition (handled above)
        return new Token(TokenType.End_of_input, "", line, pos); // Placeholder for error handling
        //return new Token(TokenType.Integer, "" + n, line, pos);
    }

//        this.line = line;
//        //this.pos = pos;
//        char c = getNextChar(); // skip opening quote
//        int n = (int)c;
//
//        // code here
//        if (getNextChar() == '\'') {
//            return new Token(TokenType.Integer, "" + n, line, pos);
//        } else {
//            return new Token(TokenType.End_of_input, "Integer", line, pos);
//        }
//TODO: Continue fleshing out logic on while loop
Token string_lit(char start, int line, int pos) { // handle string literals
    StringBuilder result = new StringBuilder();
    // code here

    char nextChar = getNextChar();
    //result.append(start);

    while (nextChar != start ) {
        result.append(nextChar);
        nextChar = getNextChar();
        if (nextChar == '"' || nextChar == '\'') {
            return new Token(TokenType.String, result.toString(), line, pos);
        }
    }

    return new Token(TokenType.End_of_input, "Invalid String literal", line, pos);
}

    Token div_or_comment(int line, int pos) { // handle division or comments
        char[] integers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        StringBuilder sb = new StringBuilder();

        for (char ints : integers) {
            if (getNextChar() == ints) {
                char nextChar = getNextChar();
                if (nextChar == '\\') {
                    char nextNextChar = getNextChar();
                    sb.append(ints);
                    sb.append(nextChar);
                    if (nextNextChar == ints) {
                        sb.append(nextNextChar);
                        return new Token(TokenType.Op_divide, "", line, pos);
                    }
                } else if (nextChar == '*') {
                    while (true) {
                        char nextNextChar = getNextChar();
                        if (nextNextChar == '*' && getNextChar() == '\\') {
                            break;
                        }
                    }
                }

            }
        }
//
//
//
            return getToken();
    }
    Token identifier_or_integer(int line, int pos) { // handle identifiers and integers
        boolean is_number = true;
        String text = "";
        // code here
        getNextChar();
        return new Token(TokenType.Identifier, text, line, pos);
    }
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
                getNextChar();
                return new Token(TokenType.Op_divide, "/", line, pos);
            case '%':
                getNextChar();
                return new Token(TokenType.Op_mod, "%", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.Op_add, "+", line, pos);
            case '-':
                getNextChar();
                return new Token(TokenType.Op_subtract, "-", line, pos);
            case '!':
                getNextChar();
                return new Token(TokenType.Op_not, "!", line, pos);
            case '<':
                getNextChar();
                return new Token(TokenType.Op_less, "<", line, pos);
            case '\u2264':
                getNextChar();
                return new Token(TokenType.Op_lessequal, "≤", line, pos);
            case '>':
                getNextChar();
                return new Token(TokenType.Op_greater, ">", line, pos);
            case '\u2265':
                getNextChar();
                return new Token(TokenType.Op_greaterequal, "≥", line, pos);
            case '\u003D':
                getNextChar();
                return new Token(TokenType.Op_equal, "⩵", line, pos);
            case '\u2260':
                getNextChar();
                return new Token(TokenType.Op_notequal, "≠", line, pos);
//            case '=':
//                return new Token(TokenType.Op_assign, "=", this.line, this.pos);
            case '\u2229':
                getNextChar();
                return new Token(TokenType.Op_and, "∧", line, pos);
            case '\u222A':
                getNextChar();
                return new Token(TokenType.Op_or, "∨", line, pos);
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


            default:
                return identifier_or_integer(line, pos);
        }
    }

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

    static void outputToFile(String result) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/hello.lex");
            myWriter.write(result);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public char getChr() {
//        Lexer myLexer = new Lexer()
//        return chr;
//    }

//    public String getS() {
//        return s;
//    }

    public static void main(String[] args) {
        if (1==1) {
            try {

                File f = new File("src/main/resources/count.c");
                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                outputToFile(result);

            } catch(FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            error(-1, -1, "No args");
        }
    }
}
