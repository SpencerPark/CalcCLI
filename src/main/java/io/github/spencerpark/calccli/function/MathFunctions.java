package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.objects.CalcDecimal;
import io.github.spencerpark.calccli.expression.objects.CalcObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MathFunctions {
    private static class DoubleMathFunction implements Function {
        private final FunctionSignature signature;
        private final java.util.function.Function<Double, Double> function;

        public DoubleMathFunction(String name, java.util.function.Function<Double, Double> function) {
            this.signature = new FunctionSignature(name, "x");
            this.function = function;
        }

        @Override
        public CalcObject call(CalcObject[] args) {
            return new CalcDecimal(function.apply(args[0].getAsDecimal()));
        }

        @Override
        public FunctionSignature getSignature() {
            return signature;
        }
    }

    private static class DoubleBiMathFunction implements Function {
        private final FunctionSignature signature;
        private final java.util.function.BiFunction<Double, Double, Double> function;

        public DoubleBiMathFunction(String name, java.util.function.BiFunction<Double, Double, Double> function) {
            this.signature = new FunctionSignature(name, "x", "y");
            this.function = function;
        }

        @Override
        public CalcObject call(CalcObject[] args) {
            return new CalcDecimal(function.apply(args[0].getAsDecimal(), args[1].getAsDecimal()));
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

    public static final Function ABS   = register(new DoubleMathFunction("abs", Math::abs));

    public static final Function SIN   = register(new DoubleMathFunction("sin", Math::sin));
    public static final Function COS   = register(new DoubleMathFunction("cos", Math::cos));
    public static final Function TAN   = register(new DoubleMathFunction("tan", Math::tan));

    public static final Function ASIN  = register(new DoubleMathFunction("asin", Math::asin));
    public static final Function ACOS  = register(new DoubleMathFunction("acos", Math::acos));
    public static final Function ATAN  = register(new DoubleMathFunction("atan", Math::atan));

    public static final Function SINH  = register(new DoubleMathFunction("sinh", Math::sinh));
    public static final Function COSH  = register(new DoubleMathFunction("cosh", Math::cosh));
    public static final Function TANH  = register(new DoubleMathFunction("tanh", Math::tanh));

    public static final Function SQRT  = register(new DoubleMathFunction("sqrt", Math::sqrt));
    public static final Function CBRT  = register(new DoubleMathFunction("cbrt", Math::cbrt));
    public static final Function LN    = register(new DoubleMathFunction("ln", Math::log));
    public static final Function LOG   = register(new DoubleMathFunction("log", Math::log10));
    public static final Function EXP   = register(new DoubleMathFunction("exp", Math::exp));

    public static final Function RAD   = register(new DoubleMathFunction("rad", Math::toRadians));
    public static final Function DEG   = register(new DoubleMathFunction("deg", Math::toDegrees));

    public static final Function FLOOR = register(new DoubleMathFunction("floor", Math::floor));
    public static final Function CEIL  = register(new DoubleMathFunction("ceil", Math::ceil));

    public static final Function RAND = register(new DoubleMathFunction("rand", (arg) -> Math.random() * arg));

    public static final Function FAC = register(new DoubleMathFunction("fac", arg -> {
        if (arg < 0)
            throw new IllegalArgumentException("Math error: Factorial of negative number "+arg);
        double res = 1d;
        for (int i = 1; i <= Math.round(arg); i++) {
            res *= i;
        }
        return res;
    }));

    public static final Function CHOOSE = register(new DoubleBiMathFunction("choose",
            (n, k) -> {
                if (k < 0 || k > n) return 0.0;
                if (k > n/2) {
                    k = n - k;
                }

                double answer = 1.0;
                for (int i = 1; i <= k; i++) {
                    answer *= (n + 1 - i);
                    answer /= i;
                }
                return answer;
            }
    ));

    public static final Function POW = register(new DoubleBiMathFunction("pow", Math::pow));

    public static final Function MAX = register(new DoubleBiMathFunction("max", Math::max));
    public static final Function MIN = register(new DoubleBiMathFunction("min", Math::min));

    public static final Function HYPOT = register(new DoubleBiMathFunction("hypot", Math::hypot));
}
