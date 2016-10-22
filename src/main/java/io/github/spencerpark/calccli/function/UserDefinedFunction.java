package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;

import java.util.Arrays;

public class UserDefinedFunction<R> implements Function<R> {
    private final String[] params;
    private final Expression<R> expression;
    private final FunctionSignature signature;

    public UserDefinedFunction(String name, String[] params, Expression<R> expression) {
        this.params = params;
        this.expression = expression;
        //TODO in parser break params into types with a type descriptor
        //temporary rebase to bring this up to date with the new function types
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) paramTypes[i] = Double.class;
        signature = new FunctionSignature(name, paramTypes);
    }

    @Override
    public R call(Environment env, Expression<?>[] args) {
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
            environment.setVariable(params[i], args[i].evaluate(environment));
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
