package net.declension.ea.cards.ohhell.nodes;


import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.utils.Validation.requireNonNullParam;

public class BinaryNode<I, C> extends Node<I, C> {
    private final Operator operator;

    enum Operator {
        ADD("+", Double::sum),
        SUBTRACT("-", (l, r) -> l - r),
        MULTIPLY("*", (l, r) -> l * r),
        DIVIDE("/", (l, r) -> r == 0 ? Double.POSITIVE_INFINITY : l / r),
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
        requireNonNullParam(operator, "Binary Operator");
        this.operator = operator;
    }


    /**
     * Convenience constructor to initialise with the children pre-set
     * @param operator
     * @param left
     * @param right
     */
    public static <I, C> BinaryNode<I, C> binary(Operator operator, Node<I, C> left, Node<I, C> right) {
        BinaryNode<I, C> ret = new BinaryNode<>(operator);
        List<Node<I, C>> ts = asList(left, right);
        ret.setChildren(ts);
        return ret;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public <T extends Node<I, C>> T mutatedCopy(Random rng) {
        T mutant = (T) new BinaryNode<>(pickRandomEnum(rng, Operator.class));
        mutant.setChildren(children);
        return mutant;
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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }

        BinaryNode that = (BinaryNode) other;
        return operator == that.operator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
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
