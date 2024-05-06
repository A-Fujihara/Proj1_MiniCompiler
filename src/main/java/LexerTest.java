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
        String inputFilePath = "src/main/resources/99bottles.c"; // Update the file path as necessary
        String inputContent = Files.readString(Paths.get(inputFilePath));

        // Create a new lexer instance with the input content
        Lexer lexer = new Lexer(inputContent);

        // Verify that the lexer object is not null
        assertNotNull(lexer);

    }
    @Test
    void error() {
//
    }

    @Test
    void follow() {
    }

    @Test
    void test_char_lit_valid() {
        String testString = "'a'";
        int line = 1;
        int pos = 1;

        lexer.s = testString; // Put test string into Lexer (not ideal, but works for unit testing)
        Lexer.Token token = lexer.char_lit(line, pos);

        assertEquals(Lexer.TokenType.Integer, token.tokentype);
        assertEquals("a", token.value);
        assertEquals(line, token.line);
        assertEquals(pos, token.pos);
    }

//    @Test
//    void test_char_lit_invalid() {
//        String testString = "aa";
//        int line = 1;
//        int pos = 1;
//
//        lexer.s = testString; // Put test string into Lexer (not ideal, but works for unit testing)
//        Lexer.Token token = lexer.char_lit(line, pos);
//
//        assertNotEquals(Lexer.TokenType.Integer, token.tokentype);
//        assertNotEquals("a", token.value);
//        assertNotEquals(line, token.line);
//        assertNotEquals(pos, token.pos);
//    }
    @Test
    void test_string_lit() {
        String testString = "'Compilers are Cool'";
        int line = 1;
        int pos = 1;

        lexer.s = testString; // Put test string into Lexer (not ideal, but works for unit testing)
        char start = '\'';

        Lexer.Token token = lexer.string_lit(start, line, pos);

        assertEquals(Lexer.TokenType.String, token.tokentype);
        assertEquals("Compilers are Cool", token.value);
        assertEquals(line, token.line);
        assertEquals(pos, token.pos);
    }

    @Test
    void div_or_comment() {
        String testString = "'Compilers are Cool'";
        int line = 1;
        int pos = 1;

        lexer.s = testString; // Put test string into Lexer (not ideal, but works for unit testing)
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
