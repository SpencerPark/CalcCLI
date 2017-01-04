package io.github.spencerpark.calccli.expression.objects;

import io.github.spencerpark.calccli.function.Function;

import java.util.List;

public abstract class CalcObject {
    public enum Type {
        INTEGER,
        DECIMAL,
        LIST,
        FUNCTION
    }

    public abstract Type getType();

    public boolean isWholeNumber() {
        return false;
    }

    public boolean isDecimal() {
        return false;
    }

    public boolean isList() {
        return false;
    }

    public boolean isFunction() {
        return false;
    }

    public abstract long getAsWholeNumber();

    public abstract double getAsDecimal();

    public abstract List<CalcObject> getAsList();

    public abstract Function getAsFunction();
}
