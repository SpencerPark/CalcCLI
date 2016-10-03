package io.github.spencerpark.calccli.parser;

public class Token {
    private final SymbolType type;
    private final String text;

    public Token(SymbolType type, String text) {
        this.type = type;
        this.text = text;
    }

    public SymbolType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
