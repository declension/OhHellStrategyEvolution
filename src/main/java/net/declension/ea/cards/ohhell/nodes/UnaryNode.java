package net.declension.ea.cards.ohhell.nodes;

import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class UnaryNode<T> extends Node<T> {
    private final Operator operator;

    protected UnaryNode(Operator operator) {
        this.operator = operator;
    }

    protected UnaryNode(Operator operator, Node<T> left, Node<T> right) {
        this.operator = operator;
        children = asList(left, right);
    }

    @Override
    public Number doEvaluation(T context) {
        return compute(children.get(0), context);
    }

    protected Number compute(Node<T> left, T context) {
        Number leftVal = left.evaluate(context);
        return operator.apply(leftVal);
    }

    enum Operator {
        LOG("+", Math::log10),
        SIN("sin", Math::sin),
        FLOOR("floor", Math::floor),
        ABS("abs", Math::abs),
        SIGNUM("sgn", Math::signum)
        ;

        private final String symbol;
        private final DoubleUnaryOperator doubleOperator;

        Operator(String symbol, DoubleUnaryOperator doubleOperator) {
            this.symbol = symbol;
            this.doubleOperator = doubleOperator;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public Number apply(Number leftArg) {
            return doubleOperator.applyAsDouble(leftArg.doubleValue());
        }
    }

    @Override
    public String toString() {
        return format("%s(%s)", children.get(0), operator);
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(1);
    }
}
