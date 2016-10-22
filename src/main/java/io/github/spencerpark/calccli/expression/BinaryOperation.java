package io.github.spencerpark.calccli.expression;

public class BinaryOperation implements Expression<Double> {
    private final BinaryOperator operator;
    private final Expression<Double> left;
    private final Expression<Double> right;

    public BinaryOperation(Expression<Double> left, BinaryOperator operator,  Expression<Double> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Double evaluate(Environment env) {
        return operator.evaluate(env, left, right);
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }
}
