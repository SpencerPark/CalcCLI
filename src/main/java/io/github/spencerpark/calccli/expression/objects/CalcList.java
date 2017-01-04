package io.github.spencerpark.calccli.expression.objects;

import io.github.spencerpark.calccli.expression.Constant;
import io.github.spencerpark.calccli.function.ConstantFunctionWrapper;
import io.github.spencerpark.calccli.function.Function;

import java.util.Collections;
import java.util.List;

public class CalcList extends CalcObject {
    private List<CalcObject> val;

    public CalcList(List<CalcObject> val) {
        this.val = val;
    }

    public CalcList(CalcObject val) {
        this.val = Collections.singletonList(val);
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.LIST;
    }

    @Override
    public long getAsWholeNumber() {
        if (this.val.size() == 1) {
            return this.val.get(0).getAsWholeNumber();
        }

        throw new IllegalStateException("Cannot convert list with " + this.val.size() + " elements to a whole number.");
    }

    @Override
    public double getAsDecimal() {
        if (this.val.size() == 1) {
            return this.val.get(0).getAsDecimal();
        }

        throw new IllegalStateException("Cannot convert list with " + this.val.size() + " elements to a decimal");
    }

    @Override
    public List<CalcObject> getAsList() {
        return this.val;
    }

    @Override
    public Function getAsFunction() {
        return new ConstantFunctionWrapper(this);
    }

    @Override
    public String toString() {
        return this.val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalcList calcList = (CalcList) o;

        return val.equals(calcList.val);
    }

    @Override
    public int hashCode() {
        return val.hashCode();
    }
}
