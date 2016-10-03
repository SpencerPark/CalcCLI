package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.DoubleExpression;
import io.github.spencerpark.calccli.expression.Environment;

import java.util.Arrays;

public class UserDefinedFunction implements Function {
    private final String[] params;
    private final DoubleExpression expression;

    public UserDefinedFunction(String[] params, DoubleExpression expression) {
        this.params = params;
        this.expression = expression;
    }

    @Override
    public double call(Environment env, double[] args) {
        if (params.length > args.length)
            throw new IllegalArgumentException("Too few arguments. " +
                    "Params: " + Arrays.toString(params) + ". " +
                    "Args: " + Arrays.toString(args));
        if (params.length < args.length)
            throw new IllegalArgumentException("Too many arguments. " +
                    "Params: " + Arrays.toString(params) + ". " +
                    "Args: " + Arrays.toString(args));

        Environment environment = new Environment(env);
        for (int i = 0; i < params.length; i++) {
            environment.setVariable(params[i], args[i]);
        }

        return this.expression.eval(environment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < this.params.length; i++) {
            sb.append(this.params[i]);
            if (i != this.params.length-1)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
