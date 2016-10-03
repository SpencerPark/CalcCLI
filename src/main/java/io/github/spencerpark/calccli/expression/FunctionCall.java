package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.function.Function;

public class FunctionCall implements DoubleExpression {
    private final String functionName;
    private final DoubleExpression[] args;

    public FunctionCall(String functionName, DoubleExpression[] args) {
        this.functionName = functionName;
        this.args = args;
    }

    @Override
    public double eval(Environment environment) {
        Function f = environment.getFunctionBank().resolve(functionName);

        double[] args = new double[this.args.length];
        for (int i = 0; i < args.length; i++)
            args[i] = this.args[i].eval(environment);

        return f.call(environment, args);
    }
}
