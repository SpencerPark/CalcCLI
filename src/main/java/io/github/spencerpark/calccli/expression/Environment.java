package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.command.WorkspaceCommand;
import io.github.spencerpark.calccli.function.FunctionBank;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Environment superEnvironment;
    private final Map<String, Double> memory;
    private final FunctionBank functionBank;
    private final Map<String, WorkspaceCommand> commandBank;

    public Environment(Environment superEnvironment) {
        this.superEnvironment = superEnvironment;
        this.functionBank = superEnvironment.getFunctionBank();
        this.memory = new HashMap<>();
        this.commandBank = new HashMap<>();
    }

    public Environment(FunctionBank functionBank) {
        this.superEnvironment = null;
        this.functionBank = functionBank;
        this.memory = new HashMap<>();
        this.commandBank = new HashMap<>();
    }

    public double getVariable(String variable) {
        Double val = memory.get(variable);
        if (val == null && superEnvironment != null)
            val = superEnvironment.getVariable(variable);
        if (val == null)
            throw new NullPointerException(variable + " is not defined.");
        return val;
    }

    public void setVariable(String variable, double value) {
        this.memory.put(variable, value);
    }

    public FunctionBank getFunctionBank() {
        return functionBank;
    }

    public void registerCommand(String name, WorkspaceCommand cmd) {
        this.commandBank.put(name, cmd);
    }

    public void executeCommand(String name, String... args) {
        WorkspaceCommand cmd = this.commandBank.get(name.toLowerCase());
        if (cmd == null) {
            System.out.println("Unknown command: "+name.toLowerCase());
        } else {
            cmd.execute(this, args);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Memory:\n");
        this.memory.forEach((var, val) ->
                sb.append('\t').append(var).append(" -> ").append(val).append('\n'));

        sb.append("Functions:\n");
        this.functionBank.forEachDefined((name, function) ->
                sb.append('\t').append(name).append(function).append('\n'));

        return sb.toString();
    }
}
