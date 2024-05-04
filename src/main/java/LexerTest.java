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
        int line = 10;
        int pos = 5;
        String errorMessage = "An error occurred";

        // Redirect System.out to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method that could potentially trigger an error
        char_lit();

        // Verify that the error message is logged correctly
        String expectedOutput = String.format("%s in line %d, pos %d\n", errorMessage, line, pos);
        assertEquals(expectedOutput, outContent.toString());

        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    void follow() {
    }

    @Test
    void char_lit() {
    }

    @Test
    void string_lit() {
    }

    @Test
    void div_or_comment() {
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
