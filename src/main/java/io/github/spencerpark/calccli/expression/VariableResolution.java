package io.github.spencerpark.calccli.expression;

public class VariableResolution implements DoubleExpression {
    private final String variable;

    public VariableResolution(String variable) {
        this.variable = variable;
    }

    @Override
    public double eval(Environment environment) {
        return environment.getVariable(variable);
    }
}
