package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.command.WorkspaceCommand;
import io.github.spencerpark.calccli.expression.objects.CalcObject;
import io.github.spencerpark.calccli.function.FunctionBank;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Memory memory;
    private final FunctionBank functionBank;
    private final Map<String, WorkspaceCommand> commandBank;

    public Environment(String name, FunctionBank functionBank) {
        this.functionBank = functionBank;
        this.memory = new Memory(name);
        this.commandBank = new HashMap<>();
    }

    private Environment(String name, Memory memory, FunctionBank functionBank) {
        this.functionBank = functionBank;
        this.memory = new Memory(name, memory);
        this.commandBank = new HashMap<>();
    }

    public Environment newNestedScope(String name) {
        return new Environment(name, new Memory(name, this.memory), this.functionBank);
    }

    public CalcObject getVariable(String variable) {
        return memory.getOrThrow(variable);
    }

    public void setVariable(String variable, CalcObject value) {
        this.memory.set(variable, value);
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
        this.memory.toString(sb);

        sb.append("Functions:\n");
        this.functionBank.forEachDefined(function ->
                sb.append('\t').append(function).append('\n'));

        return sb.toString();
    }
}
