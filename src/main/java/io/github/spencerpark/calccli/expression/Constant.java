package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public class Constant implements Expression {
    private final CalcObject val;

    public Constant(CalcObject val) {
        this.val = val;
    }

    @Override
    public CalcObject evaluate(Environment env) {
        return val;
    }
}
