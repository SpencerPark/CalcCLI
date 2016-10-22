package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;

public interface Function<R> {

    R call(Environment env, Expression<?>... args);

    FunctionSignature getSignature();

    default void checkCall(Expression<?>[] args) {
        getSignature().checkCanCallWith(args);
    }
}
