package io.github.spencerpark.calccli.command;

import io.github.spencerpark.calccli.expression.Environment;

public interface WorkspaceCommand {

    void execute(Environment env, String... args);
}
