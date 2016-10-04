package io.github.spencerpark.calccli.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class ReaderCharStream implements CharStream {
    private final Reader in;
    private char current;
    private boolean closed;

    public ReaderCharStream(InputStream in) {
        this.in = new InputStreamReader(in, Charset.defaultCharset());
        this.closed = false;
        next();
    }

    @Override
    public char peek() {
        if (this.closed) return '\0';
        return current;
    }

    @Override
    public char next() {
        if (this.closed) return '\0';
        try {
            int next = in.read();
            if (next == -1) {
                this.closed = true;
                return '\0';
            } else {
                char consumed = this.current;
                this.current = (char) next;
                return consumed;
            }
        } catch (IOException e) {
            throw new ParseException(String.format("Error reading from stream. Message: %s\n", e.getMessage()));
        }
    }

    @Override
    public boolean hasNext() {
        return !this.closed;
    }

    public void close() throws IOException {
        if (!closed) {
            closed = true;
            in.close();
        }
    }
}
