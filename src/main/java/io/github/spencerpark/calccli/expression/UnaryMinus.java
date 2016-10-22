package io.github.spencerpark.calccli.expression;

public class UnaryMinus implements Expression<Double> {
    private final Expression<Double> expression;

    public UnaryMinus(Expression<Double> expression) {
        this.expression = expression;
    }

    @Override
    public Double evaluate(Environment environment) {
        return -expression.evaluate(environment);
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }
}
