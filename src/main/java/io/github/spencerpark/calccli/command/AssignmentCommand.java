package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.Expression;

public class AssignmentCommand implements Command {
    private final String variable;
    private final Expression value;

    public AssignmentCommand(String variable, Expression value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void execute(Environment environment) {
        environment.setVariable(variable, value.evaluate(environment));
    }
}
