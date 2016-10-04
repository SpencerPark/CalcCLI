package io.github.spencerpark.calccli.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FunctionBank {
    private final Map<String, Function> functions;
    private final Map<String, UserDefinedFunction> customFunctions;

    public FunctionBank() {
        this.functions = new HashMap<>();
        this.customFunctions = new HashMap<>();
        addMathFunctions();
    }

    private void addMathFunctions() {
        functions.put("abs", MathFunctions.ABS);

        functions.put("sin", MathFunctions.SIN);
        functions.put("cos", MathFunctions.COS);
        functions.put("tan", MathFunctions.TAN);

        functions.put("asin", MathFunctions.ASIN);
        functions.put("acos", MathFunctions.ACOS);
        functions.put("atan", MathFunctions.ATAN);

        functions.put("sinh", MathFunctions.SINH);
        functions.put("cosh", MathFunctions.COSH);
        functions.put("tanh", MathFunctions.TANH);

        functions.put("sqrt", MathFunctions.SQRT);
        functions.put("cbrt", MathFunctions.CBRT);
        functions.put("ln", MathFunctions.LN);
        functions.put("log", MathFunctions.LOG);
        functions.put("exp", MathFunctions.EXP);

        functions.put("rad", MathFunctions.RAD);
        functions.put("deg", MathFunctions.DEG);

        functions.put("floor", MathFunctions.FLOOR);
        functions.put("ceil", MathFunctions.CEIL);

        functions.put("rand", MathFunctions.RAND);

        functions.put("fac", MathFunctions.FAC);

        functions.put("C", MathFunctions.CHOOSE);
        functions.put("pow", MathFunctions.POW);

        functions.put("max", MathFunctions.MAX);
        functions.put("min", MathFunctions.MIN);

        functions.put("hypot", MathFunctions.HYPOT);
    }

    public void define(String name, UserDefinedFunction function) {
        this.functions.put(name, function);
        this.customFunctions.put(name, function);
    }

    public Function resolve(String name) {
        Function fn = this.functions.get(name);
        if (fn == null)
            throw new NullPointerException("Function named " + name + " is not defined.");
        return fn;
    }

    public void forEachDefined(BiConsumer<String, UserDefinedFunction> apply) {
        this.customFunctions.forEach(apply);
    }
}
