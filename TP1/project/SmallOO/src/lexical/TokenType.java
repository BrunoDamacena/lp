package lexical;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    OPEN_CUR,
    CLOSE_CUR,
    DOT_COMMA,
    DOT,
    ASSIGN,
    OPEN_PAR,
    CLOSE_PAR,
    COMMA,
     
    // keywords
    IF,
    ELSE,
    WHILE,
    FUNCTION,
    RETURN,
    SYSTEM,
    SELF,
    ARGS,

    // operators
    NOT,
    AND,
    OR,
    EQUAL,
    DIFF,
    LOWER,
    GREATER,
    LOWER_EQUAL,
    GREATER_EQUAL,
    
    
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    
    // others
    NAME,
    NUMBER,
    STRING,

};
