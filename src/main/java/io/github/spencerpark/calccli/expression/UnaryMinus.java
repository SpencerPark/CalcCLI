package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.*;

import java.util.stream.Collectors;

public class UnaryMinus implements Expression {
    private final Expression expression;

    public UnaryMinus(Expression expression) {
        this.expression = expression;
    }

    @Override
    public CalcObject evaluate(Environment environment) {
        CalcObject val = expression.evaluate(environment);
        return negate(val);
    }

    private static CalcObject negate(CalcObject val) {
        switch (val.getType()) {
            case INTEGER:
                return new CalcInt(-val.getAsWholeNumber());
            case DECIMAL:
                return new CalcDecimal(-val.getAsDecimal());
            case LIST:
                return new CalcList(val.getAsList().stream().map(UnaryMinus::negate).collect(Collectors.toList()));
            case FUNCTION:
                throw new BadTypeException(CalcObject.Type.FUNCTION, "Cannot negate a function");
            default:
                throw new BadTypeException(val.getType(), "Cannot negate a " + val.getType().name());
        }
    }
}
