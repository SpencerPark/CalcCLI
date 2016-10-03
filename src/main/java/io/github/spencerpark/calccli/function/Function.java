package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Environment;

public interface Function {

    double call(Environment env, double[] args);
}
