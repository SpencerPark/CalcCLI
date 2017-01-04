package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcObject;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Memory {
    private final String name;
    private final Memory superTable;
    //The `declarations` are the actual `name -> value` mappings.
    private final Map<String, CalcObject> declarations;

    public Memory(String name) {
        this.name = name;
        this.superTable = null;
        declarations = new HashMap<>();
    }

    public Memory(String name, Memory superTable) {
        this.name = name;
        this.superTable = superTable;
        declarations = new HashMap<>();
    }

    public void set(String identifier, CalcObject value) {
        this.declarations.put(identifier, value);
    }

    public CalcObject get(String identifier) {
        CalcObject value = declarations.get(identifier);

        if (value == null && superTable != null)
            return superTable.get(identifier);

        return value;
    }

    public CalcObject getOrThrow(String identifier) {
        CalcObject value = get(identifier);

        if (value == null)
            throw new NullPointerException(identifier);

        return value;
    }

    public void checkExists(String identifier) {
        Object value = get(identifier);
        if (value == null)
            throw new NullPointerException(identifier);
    }

    public int countReferences() {
        return this.declarations.size()
                + ( this.superTable != null ? this.superTable.countReferences() : 0 );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    public void toString(StringBuilder sb) {
        sb.append("References: ").append(this.countReferences()).append('\n');
        dumpInternal(1, sb);
    }

    private void dumpInternal(int indentation, StringBuilder sb) {
        for (int i = 0; i < indentation; i++) sb.append("  ");
        sb.append("Scope: ").append(name).append('\n');
        this.declarations.forEach((id, val) -> {
            for (int i = 0; i < indentation; i++)
                sb.append('\t');
            sb.append(id)
                    .append(" -> ")
                    .append(String.valueOf(val))
                    .append('\n');
        });
        if (superTable != null) {
            superTable.dumpInternal(indentation + 1, sb);
        }
    }
}