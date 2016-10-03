package io.github.spencerpark.calccli.expression;

public class BinaryOperation implements DoubleExpression {
    private final BinaryOperator operator;
    private final DoubleExpression left;
    private final DoubleExpression right;

    public BinaryOperation(DoubleExpression left, BinaryOperator operator,  DoubleExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double eval(Environment env) {
        return operator.eval(env, left, right);
    }
}
