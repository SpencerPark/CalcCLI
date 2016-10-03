package io.github.spencerpark.calccli.expression;

public class UnaryMinus implements DoubleExpression {
    private final DoubleExpression expression;

    public UnaryMinus(DoubleExpression expression) {
        this.expression = expression;
    }

    @Override
    public double eval(Environment environment) {
        return -expression.eval(environment);
    }
}
