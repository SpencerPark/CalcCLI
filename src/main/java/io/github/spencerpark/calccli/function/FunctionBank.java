package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FunctionBank {
    private final Map<String, Map<Integer, Function>> functions;
    private final Map<String, Map<Integer, UserDefinedFunction>> customFunctions;

    public FunctionBank() {
        this.functions = new HashMap<>();
        this.customFunctions = new HashMap<>();
        MathFunctions.forEach(f -> putFunction(functions, f));
    }

    private static <T extends Function> void putFunction(Map<String, Map<Integer, T>> bank, T f) {
        FunctionSignature signature = f.getSignature();
        bank.compute(signature.getName(), (name, map) -> {
            if (map == null) map = new LinkedHashMap<>();
            map.put(signature.numParams(), f);
            return map;
        });
    }

    private static <T> T getFunction(Map<String, Map<Integer, T>> bank, String name, int numParams) {
        Map<Integer, T> overloads = bank.get(name);
        if (overloads == null) return null;

        return overloads.get(numParams);
    }

    public void define(UserDefinedFunction function) {
        putFunction(this.functions, function);
        putFunction(this.customFunctions, function);
    }

    public Function resolve(String name, int numParams) {
        Function fn = getFunction(this.functions, name, numParams);
        if (fn == null)
            throw new NullPointerException("No function named " + name + " that accepts " + numParams + " arguments.");
        return fn;
    }

    public CalcObject call(String name, CalcObject... args) {
        return resolve(name, args.length).call(args);
    }

    public void forEachDefault(Consumer<Function> apply) {
        this.functions.forEach((name, map) -> map.values().forEach(apply));
    }

    public void forEachDefined(Consumer<UserDefinedFunction> apply) {
        this.customFunctions.forEach((name, map) -> map.values().forEach(apply));
    }
}
