package io.github.spencerpark.calccli.function;

import io.github.spencerpark.calccli.expression.Expression;

import java.util.Arrays;

public class FunctionSignature {
    private final String name;
    //Don't expose this array, it needs to be final
    private final Class<?>[] args;

    public FunctionSignature(String name, Class<?>[] params) {
        this.name = name;
        this.args = params;
    }

    public String getName() {
        return name;
    }

    public int numArgs() {
        return this.args.length;
    }

    public Class<?> getParamType(int paramIndex) {
        if (paramIndex >= this.args.length)
            throw new IllegalArgumentException(String.format("paramIndex=%d out of bounds. Number of parameters is %d",
                    paramIndex, this.args.length));

        return this.args[paramIndex];
    }

    /**
     * Check if a function with this signature can be called with the given arguments.
     * @param args the arguments
     * @throws IllegalArgumentException if it cannot be called. This will contain a description
     * of why
     */
    public void checkCanCallWith(Expression<?>[] args) throws IllegalArgumentException {
        if (this.args.length > args.length)
            throw new IllegalArgumentException("Too few arguments. " +
                    "Params: " + Arrays.toString(this.args) + ". " +
                    "Args: " + Arrays.toString(args));
        if (this.args.length < args.length)
            throw new IllegalArgumentException("Too many arguments. " +
                    "Params: " + Arrays.toString(this.args) + ". " +
                    "Args: " + Arrays.toString(args));


        for (int i = 0; i < args.length; i++) {
            if (!this.args[i].isAssignableFrom(args[i].getType()))
                throw new IllegalArgumentException("Illegal argument " + i + " type. " +
                        "Was: " + args[i].getType().getSimpleName() + ". " +
                        "Expected: " + this.args[i].getSimpleName() + ".");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionSignature that = (FunctionSignature) o;

        if (!name.equals(that.name)) return false;
        if (this.args.length != that.args.length) return false;

        for (int arg = 0; arg < this.args.length; arg++) {
            if (!this.args[arg].isAssignableFrom(that.args[arg])) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        //It is unfortunate but we can't use the classes hash in the hash because
        //equals() uses isAssignableFrom()
        int result = name.hashCode();
        result = 31 * result + args.length;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);

        sb.append("(");

        if (args.length != 0) {
            sb.append(args[0].getSimpleName());
            for (int i = 1; i < args.length; i++)
                sb.append(", ").append(args[i]);
        }

        sb.append(")");

        return sb.toString();
    }
}
