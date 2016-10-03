package io.github.spencerpark.calccli.parser;

public class StringCharStream implements CharStream {
    private final CharSequence in;
    private int pos;

    public StringCharStream(CharSequence in) {
        this.in = in;
        this.pos = 0;
    }

    @Override
    public char peek() {
        return in.charAt(pos);
    }

    @Override
    public char next() {
        return in.charAt(pos++);
    }

    @Override
    public boolean hasNext() {
        return pos < in.length();
    }
}
