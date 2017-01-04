package io.github.spencerpark.calccli.expression.objects;

import io.github.spencerpark.calccli.expression.Constant;
import io.github.spencerpark.calccli.function.ConstantFunctionWrapper;
import io.github.spencerpark.calccli.function.Function;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class CalcInt extends CalcObject {
    private final long val;

    public CalcInt(long val) {
        this.val = val;
    }

    @Override
    public boolean isWholeNumber() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public long getAsWholeNumber() {
        return this.val;
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
        return Long.toString(this.val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalcInt calcInt = (CalcInt) o;

        return val == calcInt.val;
    }

    @Override
    public int hashCode() {
        return (int) (val ^ (val >>> 32));
    }
}
