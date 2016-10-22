package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.function.Function;

public class FunctionCall<R> implements Expression<R> {
    private final String functionName;
    private final Expression<?>[] args;
    private final Class<? extends R> returnType;

    public FunctionCall(String functionName, Expression<?>[] args, Class<? extends R> returnType) {
        this.functionName = functionName;
        this.args = args;
        this.returnType = returnType;
    }

    @Override
    public R evaluate(Environment environment) {
        Function<?> f = environment.getFunctionBank().resolve(functionName, args);

        Object r = f.call(environment, args);
        if (!returnType.isInstance(r))
                throw new ClassCastException(String.format("%s returned a %s not %s",
                        functionName, r.getClass().getSimpleName(), returnType.getSimpleName()));

        return (R) r;
    }

    @Override
    public Class<? extends R> getType() {
        return returnType;
    }
}
