package io.github.spencerpark.calccli.expression;

public enum BinaryOperator {
    PLUS {
        @Override
        public double eval(Environment env, DoubleExpression left, DoubleExpression right) {
            return left.eval(env) + right.eval(env);
        }
    },
    MINUS {
        @Override
        public double eval(Environment env, DoubleExpression left, DoubleExpression right) {
            return left.eval(env) - right.eval(env);
        }
    },
    TIMES {
        @Override
        public double eval(Environment env, DoubleExpression left, DoubleExpression right) {
            return left.eval(env) * right.eval(env);
        }
    },
    DIVIDE {
        @Override
        public double eval(Environment env, DoubleExpression left, DoubleExpression right) {
            return left.eval(env) / right.eval(env);
        }
    };

    public abstract double eval(Environment env, DoubleExpression left, DoubleExpression right);
}
