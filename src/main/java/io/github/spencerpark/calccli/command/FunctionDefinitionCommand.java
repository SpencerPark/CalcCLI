package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.function.UserDefinedFunction;

public class FunctionDefinitionCommand implements Command {
    private final UserDefinedFunction function;

    public FunctionDefinitionCommand(UserDefinedFunction function) {
        this.function = function;
    }

    @Override
    public void execute(Environment environment) {
        environment.getFunctionBank().define(function);
    }
}
