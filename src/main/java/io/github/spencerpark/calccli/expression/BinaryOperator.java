package io.github.spencerpark.calccli.expression;

public enum BinaryOperator {
    PLUS {
        @Override
        public Double evaluate(Environment env, Expression<Double> left, Expression<Double> right) {
            return left.evaluate(env) + right.evaluate(env);
        }
    },
    MINUS {
        @Override
        public Double evaluate(Environment env, Expression<Double> left, Expression<Double> right) {
            return left.evaluate(env) - right.evaluate(env);
        }
    },
    TIMES {
        @Override
        public Double evaluate(Environment env, Expression<Double> left, Expression<Double> right) {
            return left.evaluate(env) * right.evaluate(env);
        }
    },
    DIVIDE {
        @Override
        public Double evaluate(Environment env, Expression<Double> left, Expression<Double> right) {
            return left.evaluate(env) / right.evaluate(env);
        }
    };

    public abstract Double evaluate(Environment env, Expression<Double> left, Expression<Double> right);
}
