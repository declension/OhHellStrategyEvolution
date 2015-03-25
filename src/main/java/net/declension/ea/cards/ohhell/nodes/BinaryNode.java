package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.ListNumber;

import java.util.Optional;
import java.util.function.DoubleBinaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.data.SingleItemListNumber.singleItemOf;

public class BinaryNode<T> extends Node<T> {
    private final Operator operator;

    enum Operator {
        ADD("+", Double::sum),
        SUBTRACT("-", (l, r) -> l - r),
        MULTIPLY("*", (l, r) -> l * r),
        DIVIDE("/", (l, r) -> r == 0 ? Double.MIN_VALUE : l / r),
        EXPONENTIATE("^", Math::pow);

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

        public ListNumber apply(ListNumber leftArg, ListNumber rightArg) {
            return singleItemOf(doubleOperator.applyAsDouble(leftArg.doubleValue(), rightArg.doubleValue()));
        }
    }

    protected BinaryNode(Operator operator) {
        this.operator = operator;
    }

    protected BinaryNode(Operator operator, Node<T> left, Node<T> right) {
        this.operator = operator;
        children = asList(left, right);
    }

    @Override
    public ListNumber evaluate(T context) {
        checkChildren();
        return compute(children.get(0), children.get(1), context);
    }

    protected ListNumber compute(Node<T> left, Node<T> right, T context) {
        return operator.apply(left.evaluate(context), right.evaluate(context));
    }

    @Override
    public String toString() {
        return format("(%s %s %s)", children.get(0), operator, children.get(1));
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }
}