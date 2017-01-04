package io.github.spencerpark.calccli.expression;

import io.github.spencerpark.calccli.expression.objects.CalcDecimal;
import io.github.spencerpark.calccli.expression.objects.CalcInt;
import io.github.spencerpark.calccli.expression.objects.CalcObject;

public enum BinaryOperator {
    PLUS {
        @Override
        public CalcObject evaluate(CalcObject left, CalcObject right) {
            if (left.isWholeNumber() && right.isWholeNumber())
                return new CalcInt(left.getAsWholeNumber() + right.getAsWholeNumber());

            return new CalcDecimal(left.getAsDecimal() + right.getAsDecimal());
        }
    },
    MINUS {
        @Override
        public CalcObject evaluate(CalcObject left, CalcObject right) {
            if (left.isWholeNumber() && right.isWholeNumber())
                return new CalcInt(left.getAsWholeNumber() - right.getAsWholeNumber());

            return new CalcDecimal(left.getAsDecimal() - right.getAsDecimal());
        }
    },
    TIMES {
        @Override
        public CalcObject evaluate(CalcObject left, CalcObject right) {
            if (left.isWholeNumber() && right.isWholeNumber())
                return new CalcInt(left.getAsWholeNumber() * right.getAsWholeNumber());

            return new CalcDecimal(left.getAsDecimal() * right.getAsDecimal());
        }
    },
    DIVIDE {
        @Override
        public CalcObject evaluate(CalcObject left, CalcObject right) {
            if (left.isWholeNumber() && right.isWholeNumber())
                return new CalcInt(left.getAsWholeNumber() / right.getAsWholeNumber());

            return new CalcDecimal(left.getAsDecimal() / right.getAsDecimal());
        }
    };

    public abstract CalcObject evaluate(CalcObject left, CalcObject right);
}
