package io.github.spencerpark.calccli.expression;

public class Constant<T> implements Expression<T> {
    private final T val;

    public Constant(T val) {
        this.val = val;
    }

    @Override
    public Class<? extends T> getType() {
        return (Class<? extends T>) val.getClass();
    }

    @Override
    public T evaluate(Environment env) {
        return val;
    }
}
