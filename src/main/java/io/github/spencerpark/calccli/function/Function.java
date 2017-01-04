package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public interface Function {

    CalcObject call(CalcObject... args);

    FunctionSignature getSignature();

    default void checkCall(CalcObject[] args) {
        getSignature().checkCanCallWith(args);
    }
}
