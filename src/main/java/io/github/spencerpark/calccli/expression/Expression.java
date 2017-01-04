package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public interface Expression {

    /**
     * Evaluate the expression in a given environment.
     * @param environment the environment to evaluate in
     * @return the evaluated expression
     */
    CalcObject evaluate(Environment environment);
}
