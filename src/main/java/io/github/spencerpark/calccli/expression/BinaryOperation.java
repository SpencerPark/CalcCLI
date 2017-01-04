package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public class BinaryOperation implements Expression {
    private final BinaryOperator operator;
    private final Expression left;
    private final Expression right;

    public BinaryOperation(Expression left, BinaryOperator operator,  Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public CalcObject evaluate(Environment env) {
        return operator.evaluate(left.evaluate(env), right.evaluate(env));
    }
}
