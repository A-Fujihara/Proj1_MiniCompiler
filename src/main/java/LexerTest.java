// (5) Test methods provided for this class
// 1. Constructor
// 2. error()
// 3. setUp()
// 4. test_char_lit_valid()
// 5. test_string_lit()
// 6. test_div_or_comment()


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    private File inputFile;
    private Lexer lexer;

    @BeforeEach
    void setUp() throws IOException {
        // Read the content of the input file into a string
        String inputFilePath = "src/main/resources/99bottles.c";
        String inputContent = Files.readString(Paths.get(inputFilePath));

        // Create a new lexer instance with the input content
        lexer = new Lexer(inputContent);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void constructor() throws IOException {
        String inputFilePath = "src/main/resources/99bottles.c";
        String inputContent = Files.readString(Paths.get(inputFilePath));
        Lexer lexer = new Lexer(inputContent);
        assertNotNull(lexer);
    }

    @Test
    void error() {
        assertNotEquals(8, lexer.getNextChar());
    }

    @Test
    void follow() {
    }

    @Test
    void test_char_lit_valid() {
        String testString = "'a'";
        int line = 1;
        int pos = 1;

        lexer.s = testString;
        Lexer.Token token = lexer.char_lit(line, pos);

        assertEquals(Lexer.TokenType.Integer, token.tokentype);
        assertEquals("a", token.value);
        assertEquals(line, token.line);
        assertEquals(pos, token.pos);
    }

    @Test
    void test_string_lit() {
        String testString = "'Compilers are Cool'";
        int line = 1;
        int pos = 1;
        lexer.s = testString;
        char start = '\'';

        Lexer.Token token = lexer.string_lit(start, line, pos);

        assertEquals(Lexer.TokenType.String, token.tokentype);
        assertEquals("Compilers are Cool", token.value);
        assertEquals(line, token.line);
        assertEquals(pos, token.pos);
    }

    @Test
    void test_div_or_comment() {
        String testString = "'Compilers are Cool'";
        int line = 1;
        int pos = 1;

        lexer.s = testString;
        char start = '\'';

        Lexer.Token token = lexer.string_lit(start, line, pos);

        assertEquals(Lexer.TokenType.String, token.tokentype);
        assertEquals("Compilers are Cool", token.value);
        assertEquals(line, token.line);
        assertEquals(pos, token.pos);
    }

    @Test
    void identifier_or_integer() {
    }

    @Test
    void getToken() {
    }

    @Test
    void getNextChar() {

    }

    @Test
    void printTokens() {
    }

    @Test
    void outputToFile() {
    }

    @Test
    void main() {
    }
}
