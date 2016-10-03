package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.function.FunctionBank;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Environment superEnvironment;
    private final Map<String, Double> memory;
    private final FunctionBank functionBank;

    public Environment(Environment superEnvironment) {
        this.superEnvironment = superEnvironment;
        this.functionBank = superEnvironment.getFunctionBank();
        this.memory = new HashMap<>();
    }

    public Environment(FunctionBank functionBank) {
        this.superEnvironment = null;
        this.functionBank = functionBank;
        this.memory = new HashMap<>();
    }

    public double getVariable(String variable) {
        Double val = memory.get(variable);
        if (val == null && superEnvironment != null)
            val = superEnvironment.getVariable(variable);
        if (val == null)
            throw new NullPointerException(variable + " is not defined.");
        return val;
    }

    public void setVariable(String variable, double value) {
        this.memory.put(variable, value);
    }

    public FunctionBank getFunctionBank() {
        return functionBank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Memory:\n");
        this.memory.forEach((var, val) ->
                sb.append('\t').append(var).append(" -> ").append(val).append('\n'));

        sb.append("Functions:\n");
        this.functionBank.forEachDefined((name, function) ->
                sb.append('\t').append(name).append(function).append('\n'));

        return sb.toString();
    }
}
