package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FunctionBank {
    private final Map<FunctionSignature, Function> functions;
    private final Map<FunctionSignature, UserDefinedFunction> customFunctions;

    public FunctionBank() {
        this.functions = new HashMap<>();
        this.customFunctions = new HashMap<>();
        MathFunctions.forEach(f -> functions.put(f.getSignature(), f));
    }

    public void define(UserDefinedFunction function) {
        this.functions.put(function.getSignature(), function);
        this.customFunctions.put(function.getSignature(), function);
    }

    public Function resolve(String name, Class<?>... params) {
        FunctionSignature signature = new FunctionSignature(name, params);
        Function fn = this.functions.get(signature);
        if (fn == null)
            throw new NullPointerException("No function with signature: " + signature);
        return fn;
    }

    public Function resolve(String name, Expression<?>... params) {
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getType();

        return resolve(name, paramTypes);
    }

    public void forEachDefault(BiConsumer<FunctionSignature, Function> apply) {
        this.functions.forEach(apply);
    }

    public void forEachDefined(BiConsumer<FunctionSignature, UserDefinedFunction> apply) {
        this.customFunctions.forEach(apply);
    }
}
