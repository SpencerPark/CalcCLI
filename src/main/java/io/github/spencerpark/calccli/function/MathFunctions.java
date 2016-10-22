package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Constant;
import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MathFunctions {
    private static class DoubleMathFunction implements Function<Double> {
        private final FunctionSignature signature;
        private final java.util.function.Function<Double, Double> function;

        public DoubleMathFunction(String name, java.util.function.Function<Double, Double> function) {
            this.signature = new FunctionSignature(name, new Class[] { Double.class });
            this.function = function;
        }

        @Override
        public Double call(Environment env, Expression<?>[] args) {
            checkCall(args);

            return function.apply((Double) args[0].evaluate(env));
        }

        @Override
        public FunctionSignature getSignature() {
            return signature;
        }
    }

    private static class DoubleBiMathFunction implements Function<Double> {
        private final FunctionSignature signature;
        private final java.util.function.BiFunction<Double, Double, Double> function;

        public DoubleBiMathFunction(String name, java.util.function.BiFunction<Double, Double, Double> function) {
            this.signature = new FunctionSignature(name, new Class[] { Double.class, Double.class });
            this.function = function;
        }

        @Override
        public Double call(Environment env, Expression<?>[] args) {
            checkCall(args);

            return function.apply((Double) args[0].evaluate(env), (Double) args[1].evaluate(env));
        }

        @Override
        public FunctionSignature getSignature() {
            return signature;
        }
    }

    private static final Set<Function> ALL = new HashSet<>();

    private static <T extends Function> T register(T f) {
        ALL.add(f);
        return f;
    }

    public static void forEach(Consumer<Function> apply) {
        ALL.forEach(apply);
    }

    public static final Function<Double> ABS   = register(new DoubleMathFunction("abs", Math::abs));

    public static final Function<Double> SIN   = register(new DoubleMathFunction("sin", Math::sin));
    public static final Function<Double> COS   = register(new DoubleMathFunction("cos", Math::cos));
    public static final Function<Double> TAN   = register(new DoubleMathFunction("tan", Math::tan));

    public static final Function<Double> ASIN  = register(new DoubleMathFunction("asin", Math::asin));
    public static final Function<Double> ACOS  = register(new DoubleMathFunction("acos", Math::acos));
    public static final Function<Double> ATAN  = register(new DoubleMathFunction("atan", Math::atan));

    public static final Function<Double> SINH  = register(new DoubleMathFunction("sinh", Math::sinh));
    public static final Function<Double> COSH  = register(new DoubleMathFunction("cosh", Math::cosh));
    public static final Function<Double> TANH  = register(new DoubleMathFunction("tanh", Math::tanh));

    public static final Function<Double> SQRT  = register(new DoubleMathFunction("sqrt", Math::sqrt));
    public static final Function<Double> CBRT  = register(new DoubleMathFunction("cbrt", Math::cbrt));
    public static final Function<Double> LN    = register(new DoubleMathFunction("ln", Math::log));
    public static final Function<Double> LOG   = register(new DoubleMathFunction("log", Math::log10));
    public static final Function<Double> EXP   = register(new DoubleMathFunction("exp", Math::exp));

    public static final Function<Double> RAD   = register(new DoubleMathFunction("rad", Math::toRadians));
    public static final Function<Double> DEG   = register(new DoubleMathFunction("deg", Math::toDegrees));

    public static final Function<Double> FLOOR = register(new DoubleMathFunction("floor", Math::floor));
    public static final Function<Double> CEIL  = register(new DoubleMathFunction("ceil", Math::ceil));

    public static final Function<Double> RAND = register(new DoubleMathFunction("rand", (arg) -> Math.random() * arg));

    public static final Function<Double> FAC = register(new DoubleMathFunction("fac", arg -> {
        if (arg < 0)
            throw new IllegalArgumentException("Math error: Factorial of negative number "+arg);
        double res = 1d;
        for (int i = 1; i <= Math.round(arg); i++) {
            res *= i;
        }
        return res;
    }));

    public static final Function<Double> CHOOSE = register(new DoubleBiMathFunction("choose",
            (n, k) -> FAC.call(null, new Constant<>(n))
                            / (FAC.call(null, new Constant<>(k)) * FAC.call(null, new Constant<>(n - k)))));

    public static final Function<Double> POW = register(new DoubleBiMathFunction("pow", Math::pow));

    public static final Function<Double> MAX = register(new DoubleBiMathFunction("max", Math::max));
    public static final Function<Double> MIN = register(new DoubleBiMathFunction("min", Math::min));

    public static final Function<Double> HYPOT = register(new DoubleBiMathFunction("hypot", Math::hypot));
}
