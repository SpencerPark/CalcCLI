package io.github.spencerpark.calccli.expression.objects;

public class BadTypeException extends RuntimeException {
    private final CalcObject.Type type;

    public BadTypeException(CalcObject.Type type, String message) {
        super(message);
        this.type = type;
    }

    public CalcObject.Type getType() {
        return type;
    }
}
