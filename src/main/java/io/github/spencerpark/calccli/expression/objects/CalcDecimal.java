package io.github.spencerpark.calccli.expression.objects;

import io.github.spencerpark.calccli.function.ConstantFunctionWrapper;
import io.github.spencerpark.calccli.function.Function;

import java.util.Collections;
import java.util.List;

public class CalcDecimal extends CalcObject {
    private final double val;

    public CalcDecimal(double val) {
        this.val = val;
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.DECIMAL;
    }

    @Override
    public long getAsWholeNumber() {
        return (long) this.val;
    }

    @Override
    public double getAsDecimal() {
        return this.val;
    }

    @Override
    public List<CalcObject> getAsList() {
        return Collections.singletonList(this);
    }

    @Override
    public Function getAsFunction() {
        return new ConstantFunctionWrapper(this);
    }

    @Override
    public String toString() {
        return String.format("%.4f", this.val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalcDecimal that = (CalcDecimal) o;

        return Double.compare(that.val, val) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(val);
        return (int) (temp ^ (temp >>> 32));
    }
}
