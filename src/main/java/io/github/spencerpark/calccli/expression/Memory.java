package io.github.spencerpark.calccli.expression;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Memory {
    private final String name;
    private final Memory superTable;
    //The `declarations` are the actual `name -> value` mappings.
    private final Map<String, Object> declarations;

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

    public void set(String identifier, Object value) {
        this.declarations.put(identifier, value);
    }

    public <T> T get(String identifier, Class<T> type) {
        Object value = get(identifier);
        if (value == null)
            return superTable == null ? null : superTable.get(identifier, type);
        return type.isInstance(value) ? (T) value : null;
    }

    public Object get(String identifier) {
        Object value = declarations.get(identifier);

        if (value == null && superTable != null)
            return superTable.get(identifier);

        return value;
    }

    public <T> T getOrThrow(String identifier, Class<T> type) {
        Object value = get(identifier);

        if (value == null)
            throw new NullPointerException(identifier);

        if (!type.isInstance(value))
            throw new ClassCastException(String.format("%s points to a %s not %s",
                    identifier, value.getClass().getSimpleName(), type.getSimpleName()));

        //Otherwise all is fine so we can safely cast and return the value
        return (T) value;
    }

    public Class<?> getType(String identifier) {
        Object value = get(identifier);
        return value == null ? null : value.getClass();
    }

    public boolean identifierTypeIs(String identifier, Class<?> type) {
        Object value = get(identifier);
        return value != null && type.isAssignableFrom(value.getClass());
    }

    public void checkType(String identifier, Class<?> type) {
        Object value = get(identifier);
        if (value != null && !type.isAssignableFrom(value.getClass()))
            throw new ClassCastException(String.format("%s points to a %s not %s",
                    identifier, value.getClass().getSimpleName(), type.getSimpleName()));
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