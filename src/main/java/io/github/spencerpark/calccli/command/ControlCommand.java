package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;

public class ControlCommand implements Command {
    private final String command;
    private final String[] args;

    public ControlCommand(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    @Override
    public void execute(Environment environment) {
        environment.executeCommand(command, args);
    }
}
