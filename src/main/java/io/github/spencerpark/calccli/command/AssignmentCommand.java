package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.DoubleExpression;
import io.github.spencerpark.calccli.expression.Environment;

public class AssignmentCommand implements Command {
    private final String variable;
    private final DoubleExpression value;

    public AssignmentCommand(String variable, DoubleExpression value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void execute(Environment environment) {
        environment.setVariable(variable, value.eval(environment));
    }
}
