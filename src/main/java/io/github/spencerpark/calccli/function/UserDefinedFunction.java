package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;
import io.github.spencerpark.calccli.expression.objects.CalcObject;

import java.util.Arrays;

public class UserDefinedFunction implements Function {
    private final Environment env;
    private final String[] params;
    private final Expression expression;
    private final FunctionSignature signature;

    public UserDefinedFunction(Environment env, String name, String[] params, Expression expression) {
        this.env = env;
        this.params = params;
        this.expression = expression;
        signature = new FunctionSignature(name, params);
    }

    @Override
    public CalcObject call(CalcObject... args) {
        if (params.length > args.length)
            throw new IllegalArgumentException("Too few arguments. " +
                    "Params: " + Arrays.toString(params) + ". " +
                    "Args: " + Arrays.toString(args));
        if (params.length < args.length)
            throw new IllegalArgumentException("Too many arguments. " +
                    "Params: " + Arrays.toString(params) + ". " +
                    "Args: " + Arrays.toString(args));

        Environment environment = env.newNestedScope(getSignature().toString());
        for (int i = 0; i < params.length; i++) {
            environment.setVariable(params[i], args[i]);
        }

        return this.expression.evaluate(environment);
    }

    @Override
    public FunctionSignature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return getSignature().toString();
    }
}
