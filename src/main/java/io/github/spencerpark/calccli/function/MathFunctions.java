package io.github.spencerpark.calccli.function;

public class MathFunctions {
    public static final SimpleFunction ABS = Math::abs;

    public static final SimpleFunction SIN = Math::sin;
    public static final SimpleFunction COS = Math::cos;
    public static final SimpleFunction TAN = Math::tan;

    public static final SimpleFunction ASIN = Math::asin;
    public static final SimpleFunction ACOS = Math::acos;
    public static final SimpleFunction ATAN = Math::atan;

    public static final SimpleFunction SINH = Math::sinh;
    public static final SimpleFunction COSH = Math::cosh;
    public static final SimpleFunction TANH = Math::tanh;

    public static final SimpleFunction SQRT = Math::sqrt;
    public static final SimpleFunction CBRT = Math::cbrt;
    public static final SimpleFunction LN = Math::log;
    public static final SimpleFunction LOG = Math::log10;
    public static final SimpleFunction EXP = Math::exp;

    public static final SimpleFunction FAC = arg -> {
        if (arg < 0)
            throw new IllegalArgumentException("Math error: Factorial of negative number "+arg);
        double res = 1d;
        for (int i = 1; i <= Math.round(arg); i++) {
            res *= i;
        }
        return res;
    };

    public static final SimpleBiFunction CHOOSE = (n, k) ->
            FAC.call(n) / ( FAC.call(k) * FAC.call(n - k) );

    public static final SimpleBiFunction POW = Math::pow;
}
