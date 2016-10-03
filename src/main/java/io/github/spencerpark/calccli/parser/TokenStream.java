package io.github.spencerpark.calccli.parser;

import io.github.spencerpark.calccli.util.ShiftingArray;

import java.util.function.Supplier;

public class TokenStream {
    private final ShiftingArray<Token> lookahead;
    private final Supplier<Token> tokens;
    private int tokensRead = 0;

    public TokenStream(int lookahead, Supplier<Token> tokens) {
        if (lookahead < 1)
            throw new IllegalArgumentException("Lookahead must be at least 1");

        this.lookahead = new ShiftingArray<>(lookahead);
        this.tokens = tokens;
    }

    public void reset() {
        this.tokensRead = 0;
    }

    private void ensureBufferInitialized() {
        while (tokensRead < lookahead.size()) {
            tokensRead++;
            lookahead.pushTail(tokens.get());
        }
    }

    public Token lookahead(int amt) {
        ensureBufferInitialized();
        return lookahead.get(amt);
    }

    public Token peek() {
        ensureBufferInitialized();
        return lookahead.get(0);
    }

    public Token consume() {
        ensureBufferInitialized();
        return lookahead.pushTail(tokens.get());
    }
}
