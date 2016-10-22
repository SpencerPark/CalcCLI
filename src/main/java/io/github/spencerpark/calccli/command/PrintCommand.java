package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;

public class PrintCommand implements Command {
    private final Expression<?> expression;

    public PrintCommand(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment environment) {
        Object val = expression.evaluate(environment);
        environment.setVariable("ans", val);
        System.out.println(">>> " + val);
    }
}
