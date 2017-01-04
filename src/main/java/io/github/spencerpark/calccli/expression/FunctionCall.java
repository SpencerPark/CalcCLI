package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

public class FunctionCall implements Expression {
    private final String functionName;
    private final Expression[] args;

    public FunctionCall(String functionName, Expression[] args) {
        this.functionName = functionName;
        this.args = args;
    }

    @Override
    public CalcObject evaluate(Environment environment) {
        return environment.getFunctionBank().call(functionName, evalArgs(environment, args));
    }

    public static CalcObject[] evalArgs(Environment env, Expression... args) {
        CalcObject[] evalArgs = new CalcObject[args.length];

        for (int i = 0; i < args.length; i++)
            evalArgs[i] = args[i].evaluate(env);

        return evalArgs;
    }
}
