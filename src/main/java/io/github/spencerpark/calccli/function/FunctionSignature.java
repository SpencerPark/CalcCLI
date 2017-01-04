package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

import java.util.Arrays;

public class FunctionSignature {
    private final String name;
    //Don't expose this array, it needs to be final
    private final String[] params;

    public FunctionSignature(String name, String... params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public int numParams() {
        return this.params.length;
    }

    /**
     * Check if a function with this signature can be called with the given arguments.
     * @param args the arguments
     * @throws IllegalArgumentException if it cannot be called. This will contain a description
     * of why
     */
    public void checkCanCallWith(CalcObject[] args) throws IllegalArgumentException {
        if (this.params.length > args.length)
            throw new IllegalArgumentException("Too few arguments. " +
                    "Params: " + Arrays.toString(this.params) + ". " +
                    "Args: " + Arrays.toString(args));
        if (this.params.length < args.length)
            throw new IllegalArgumentException("Too many arguments. " +
                    "Params: " + Arrays.toString(this.params) + ". " +
                    "Args: " + Arrays.toString(args));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionSignature that = (FunctionSignature) o;

        if (!name.equals(that.name)) return false;
        if (this.params.length != that.params.length) return false;

        return true;
    }

    @Override
    public int hashCode() {
        //It is unfortunate but we can't use the classes hash in the hash because
        //equals() uses isAssignableFrom()
        int result = name.hashCode();
        result = 31 * result + params.length;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);

        sb.append("(");

        if (params.length != 0) {
            sb.append(params[0]);
            for (int i = 1; i < params.length; i++)
                sb.append(", ").append(params[i]);
        }

        sb.append(")");

        return sb.toString();
    }
}
