# Mini Compiler - Lexer and Parser

A Java-based lexical analyzer and parser implementation for a C-like programming language. This project demonstrates compiler construction fundamentals by tokenizing source code and building abstract syntax trees (AST).

## Overview

This mini-compiler consists of two main components:
- **Lexer**: Tokenizes input source code into meaningful tokens
- **Parser**: Builds an Abstract Syntax Tree from the tokenized input

The compiler can process C-like syntax including variables, operators, control structures (if/else, while), and basic I/O operations.

## Features

- **Lexical Analysis**: Recognizes keywords, identifiers, operators, literals, and symbols
- **Syntax Analysis**: Builds AST for proper code structure validation  
- **Error Handling**: Provides detailed error messages with line and position information
- **File I/O**: Processes multiple input files and generates output files
- **Unit Testing**: Comprehensive test suite using JUnit 5

## Technologies Used

- **Java**: Core implementation language
- **JUnit 5**: Unit testing framework

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── Lexer.java          # Lexical analyzer implementation
│   │   └── Parser.java         # Parser and AST builder
│   └── resources/
│       ├── *.c                 # Sample C input files
│       └── *.lex              # Generated lexer output files
└── test/
    └── java/
        └── LexerTest.java     # Unit tests for lexer functionality
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Compiling and Running

```bash
# Compile the Java files
javac -cp . src/main/java/*.java

# Run the lexer
java -cp . Lexer

# Run the parser  
java -cp . Parser
```

### Running with an IDE

1. Open the project in your preferred Java IDE
2. Set the main class to `Lexer` or `Parser`
3. Run the project

The lexer will process sample C files from `src/main/resources/` and generate corresponding `.lex` token files.

### Running Tests

```bash
# Compile test files (with JUnit 5 in classpath)
javac -cp .:junit-platform-console-standalone.jar src/test/java/*.java

# Run tests
java -cp .:junit-platform-console-standalone.jar org.junit.platform.console.ConsoleLauncher --scan-classpath
```

Or simply run the test files directly in your IDE.

## Supported Language Features

### Keywords
- `if`, `else`, `while`
- `print`, `putc`

### Operators
- Arithmetic: `+`, `-`, `*`, `/`, `%`
- Comparison: `<`, `<=`, `>`, `>=`, `==`, `!=`
- Logical: `&&`, `||`, `!`
- Assignment: `=`

### Data Types
- Integers
- Strings (double-quoted)
- Character literals (single-quoted)
- Identifiers/variables

### Control Structures
- If-else statements
- While loops
- Code blocks with `{}`

## Example Usage

**Input C-like code:**
```c
if (x > 5) {
    print "x is greater than 5";
} else {
    print "x is 5 or less";
}
```

**Generated tokens:**
```
1    1 Keyword_if
1    4 LeftParen
1    5 Identifier     x
1    7 Op_greater
1    9 Integer        5
1   10 RightParen
1   12 LeftBrace
2    5 Keyword_print
2   11 String         "x is greater than 5"
...
```

## Testing

The project includes comprehensive unit tests covering:
- Token recognition for all supported types
- String and character literal parsing
- Comment handling (single-line `//` and multi-line `/* */`)
- Error conditions and edge cases

Run specific test methods by running the test class directly in your IDE or with:
```bash
java -cp .:junit-platform-console-standalone.jar LexerTest
```

## Output Files

The lexer generates `.lex` files containing tokenized output:
- `hello.lex` - Tokens from `prime.c`
- `hello2.lex` - Tokens from `99bottles.c`
- `hello3.lex` - Tokens from `fizzbuzz.c`

The parser generates `.par` files with AST representations.

## Future Enhancements

- Code generation backend
- Symbol table implementation
- Type checking
- Optimization passes
- Additional language constructs (functions, arrays, etc.)

## Author

Angela Fujihara - Computer Science Graduate

## License

This project is for educational purposes as part of compiler construction coursework.
