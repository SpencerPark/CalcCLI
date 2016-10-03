package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;

public interface Command {

    void execute(Environment environment);
}
