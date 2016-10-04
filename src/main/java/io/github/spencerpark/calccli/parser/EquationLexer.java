package io.github.spencerpark.calccli.parser;

import java.util.function.Function;
import java.util.function.Supplier;

public class EquationLexer implements Supplier<Token> {
    protected static final Token EOS = new Token(SymbolType.EOS, "");
    private static final Token PLUS = new Token(SymbolType.PLUS, "+");
    private static final Token MINUS = new Token(SymbolType.MINUS, "-");
    private static final Token TIMES = new Token(SymbolType.TIMES, "*");
    private static final Token DIVIDE = new Token(SymbolType.DIVIDE, "/");
    private static final Token L_PAREN = new Token(SymbolType.L_PAREN, "(");
    private static final Token R_PAREN = new Token(SymbolType.R_PAREN, ")");
    private static final Token COMMA = new Token(SymbolType.COMMA, ",");
    private static final Token SEMI_COLON = new Token(SymbolType.SEMI_COLON, ";");
    private static final Token ASSIGN = new Token(SymbolType.ASSIGN, "=");
    private static final Token DOLLAR = new Token(SymbolType.DOLLAR, "$");
    private static final Function<String, Token> NUMBER = (num) -> new Token(SymbolType.NUMBER, num);
    private static final Function<String, Token> IDENTIFIER = (id) -> new Token(SymbolType.IDENTIFIER, id);
    private static final Function<String, Token> STRING = (contents) -> new Token(SymbolType.STRING, contents);

    private static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private static boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private CharStream input;

    public EquationLexer(CharStream input) {
        this.input = input;
    }

    public void setInput(CharStream input) {
        this.input = input;
    }

    @Override
    public Token get() {
        char c;
        while (input.hasNext()) {
            c = input.next();
            switch (c) {
                case '+': return PLUS;
                case '-': return MINUS;
                case '*': return TIMES;
                case '/': return DIVIDE;
                case '(': return L_PAREN;
                case ')': return R_PAREN;
                case ',': return COMMA;
                case ';': return SEMI_COLON;
                case '=': return ASSIGN;
                case '$': return DOLLAR;
                case '"':
                    StringBuilder string = new StringBuilder();
                    while (input.hasNext() && input.peek() != '"') {
                        string.append(input.next());
                    }
                    if (!input.hasNext()) {
                        throw new ParseException("string not closed: " + string.toString());
                    }
                    input.next();
                    return STRING.apply(string.toString());
            }
            if (isDigit(c)) {
                StringBuilder digit = new StringBuilder(Character.toString(c));
                while (input.hasNext() && isDigit(input.peek()))
                    digit.append(input.next());
                if (input.hasNext() && input.peek() == '.') {
                    digit.append(input.next());
                    while (input.hasNext() && isDigit(input.peek()))
                        digit.append(input.next());
                }
                return NUMBER.apply(digit.toString());
            } else if (isLetter(c)) {
                StringBuilder id = new StringBuilder(Character.toString(c));
                while (input.hasNext() && (isLetter(input.peek()) || isDigit(input.peek())))
                    id.append(input.next());
                return IDENTIFIER.apply(id.toString());
            }
        }

        //We couldn't find a token by the end
        return EOS;
    }
}
