package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public class VariableResolution implements Expression {
    private final String variable;

    public VariableResolution(String variable) {
        this.variable = variable;
    }

    @Override
    public CalcObject evaluate(Environment environment) {
        return environment.getVariable(variable);
    }
}
