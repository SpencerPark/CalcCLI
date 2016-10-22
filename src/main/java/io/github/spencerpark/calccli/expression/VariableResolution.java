package io.github.spencerpark.calccli.expression;

public class VariableResolution implements Expression<Double> {
    private final String variable;

    public VariableResolution(String variable) {
        this.variable = variable;
    }

    @Override
    public Class<? extends Double> getType() {
        return Double.class;
    }

    @Override
    public Double evaluate(Environment environment) {
        return environment.getVariable(variable, Double.class);
    }
}
