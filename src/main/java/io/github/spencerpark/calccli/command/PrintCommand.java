package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.DoubleExpression;
import io.github.spencerpark.calccli.expression.Environment;

public class PrintCommand implements Command {
    private final DoubleExpression expression;

    public PrintCommand(DoubleExpression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment environment) {
        double val = expression.eval(environment);
        environment.setVariable("ans", val);
        System.out.println(">>> " + val);
    }
}
