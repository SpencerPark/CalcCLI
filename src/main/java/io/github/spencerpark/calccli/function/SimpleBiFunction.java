package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;

import java.util.Arrays;

@FunctionalInterface
public interface SimpleBiFunction extends Function {

    double call(double arg1, double arg2);

    @Override
    default double call(Environment env, double[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Too few arguments. 2 Expected. " +
                    "Args: " + Arrays.toString(args));
        if (args.length > 2)
            throw new IllegalArgumentException("Too many arguments. 2 Expected. " +
                    "Args: " + Arrays.toString(args));

        return call(args[0], args[1]);
    }
}
