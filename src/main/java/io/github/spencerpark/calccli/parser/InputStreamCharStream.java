package io.github.spencerpark.calccli.parser;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamCharStream implements CharStream {
    private final InputStream in;
    private char next;
    private boolean closed;

    public InputStreamCharStream(InputStream in) {
        this.in = in;
        this.closed = false;
    }

    @Override
    public char peek() {
        if (this.closed) return '\0';
        return next;
    }

    @Override
    public char next() {
        if (this.closed) return '\0';
        try {
            return this.next = (char) in.read();
        } catch (IOException e) {
            throw new ParseException(String.format("Error reading from stream. Message: %s\n", e.getMessage()));
        }
    }

    @Override
    public boolean hasNext() {
        if (this.closed) return false;
        try {
            return in.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public void close() throws IOException {
        if (!closed) {
            closed = true;
            in.close();
        }
    }
}
