package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public class ConstantFunctionWrapper implements Function {
    private final CalcObject val;
    private final FunctionSignature signature;

    public ConstantFunctionWrapper(CalcObject val) {
        this.val = val;
        this.signature = new FunctionSignature(val.toString());
    }

    @Override
    public CalcObject call(CalcObject... args) {
        return val;
    }

    @Override
    public FunctionSignature getSignature() {
        return signature;
    }
}
