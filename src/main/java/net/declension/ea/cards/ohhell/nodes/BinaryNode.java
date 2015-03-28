package net.declension.ea.cards.ohhell.nodes;


import java.util.List;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class BinaryNode<I, C> extends Node<I, C> {
    private final Operator operator;

    enum Operator {
        ADD("+", Double::sum),
        SUBTRACT("-", (l, r) -> l - r),
        MULTIPLY("*", (l, r) -> l * r),
        DIVIDE("/", (l, r) -> r == 0 ? Double.MIN_VALUE : l / r),
        EXPONENTIATE("^", Math::pow);

        static final List<Operator> ALL_BINARY_OPERATORS = asList(values());

        private final String symbol;
        private final DoubleBinaryOperator doubleOperator;

        Operator(String symbol, DoubleBinaryOperator doubleOperator) {
            this.symbol = symbol;
            this.doubleOperator = doubleOperator;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public Number apply(Number leftArg, Number rightArg) {
            return doubleOperator.applyAsDouble(leftArg.doubleValue(), rightArg.doubleValue());
        }
    }

    protected BinaryNode(Operator operator) {
        this.operator = operator;
    }

    protected BinaryNode(Operator operator, Node<I, C> left, Node<I, C> right) {
        this.operator = operator;
        children = asList(left, right);
    }

    @Override
    public Number doEvaluation(I item, C context) {
        checkChildren();
        return compute(child(0), child(1), item, context);
    }

    protected Number compute(Node<I, C> left, Node<I, C> right, I item, C context) {
        return operator.apply(left.evaluate(item, context), right.evaluate(item, context));
    }

    @Override
    public String toString() {
        return format("(%s %s %s)", child(0), operator, child(1));
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }
}
