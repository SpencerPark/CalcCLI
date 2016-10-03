package io.github.spencerpark.calccli.expression;

public class Constant implements DoubleExpression {
    private final double val;

    public Constant(double val) {
        this.val = val;
    }

    @Override
    public double eval(Environment env) {
        return val;
    }
}
