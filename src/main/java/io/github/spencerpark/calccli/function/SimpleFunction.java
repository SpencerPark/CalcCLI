package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;

import java.util.Arrays;

@FunctionalInterface
public interface SimpleFunction extends Function {

    double call(double arg);

    @Override
    default double call(Environment env, double[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("Too few arguments. 1 Expected. " +
                    "Args: " + Arrays.toString(args));
        if (args.length > 1)
            throw new IllegalArgumentException("Too many arguments. 1 Expected. " +
                    "Args: " + Arrays.toString(args));

        return call(args[0]);
    }
}
