package io.github.spencerpark.calccli.parser;

public interface CharStream {

    char peek();

    char next();

    boolean hasNext();

}
