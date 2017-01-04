package io.github.spencerpark.calccli.expression.objects;

import io.github.spencerpark.calccli.function.Function;

import java.util.List;

public class CalcFunction extends CalcObject {
    private final Function val;

    public CalcFunction(Function val) {
        this.val = val;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.FUNCTION;
    }

    @Override
    public long getAsWholeNumber() {
        return this.val.call().getAsWholeNumber();
    }

    @Override
    public double getAsDecimal() {
        return this.val.call().getAsDecimal();
    }

    @Override
    public List<CalcObject> getAsList() {
        return this.val.call().getAsList();
    }

    @Override
    public Function getAsFunction() {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val.getSignature().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalcFunction that = (CalcFunction) o;

        return val.equals(that.val);
    }

    @Override
    public int hashCode() {
        return val.hashCode();
    }
}
