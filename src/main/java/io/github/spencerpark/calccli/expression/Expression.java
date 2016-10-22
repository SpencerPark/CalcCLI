package io.github.spencerpark.calccli.expression;

public interface Expression<T> {

    /**
     * Get the type of the expression. This class should be the same as
     * the value returned by
     * {@link #evaluate(Environment)}.{@link Object#getClass() getClass()}
     * @return the type of the result of evaluating this expression
     */
    Class<? extends T> getType();

    /**
     * Evaluate the expression in a given environment.
     * @param environment the environment to evaluate in
     * @return the evaluated expression
     */
    T evaluate(Environment environment);
}
