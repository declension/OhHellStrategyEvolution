package net.declension.ea.cards.ohhell.nodes;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.DoubleBinaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.utils.Validation.requireNonNullParam;

public class BinaryNode<I, C> extends Node<I, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryNode.class);
    private Operator operator;

    public enum Operator {
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

    /**
     * Construct a binary operator node
     * @param operator the operator used.
     */
    public BinaryNode(Operator operator) {
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

    /**
     * Gets the operator in use
     * @return the operator type
     */
    Operator getOperator() {
        return operator;
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new BinaryNode<I,C>(operator);
    }

    @Override
    public Node<I,C> mutate(Random rng) {

        Operator newOperator;
        do {
            newOperator = pickRandomEnum(rng, Operator.class);
        } while (Operator.ALL_BINARY_OPERATORS.size() > 1 && newOperator == operator);
        LOGGER.debug("Mutating {}: {} -> {}", this, operator, newOperator);
        operator = newOperator;
        return this;
    }

    @Override
    public Number doEvaluation(I item, C context) {
        return compute(child(0), child(1), item, context);
    }

    protected Number compute(Node<I, C> left, Node<I, C> right, I item, C context) {
        return operator.apply(left.evaluate(item, context), right.evaluate(item, context));
    }

    @Override
    public Node<I, C> simplifiedVersion() {
        Node<I, C> left = child(0);
        Node<I, C> simpleLeft = left.simplifiedVersion();
        Node<I, C> right = child(1);
        Node<I, C> simpleRight = right.simplifiedVersion();
        if (simpleLeft instanceof ConstantNode && simpleRight instanceof ConstantNode) {
            Number leftResult = ((ConstantNode) simpleLeft).getValue();
            Number rightResult = ((ConstantNode) simpleRight).getValue();
            Number result = operator.apply(leftResult, rightResult);
            return new ConstantNode<>(
                    leftResult instanceof Integer && rightResult instanceof Integer? result.intValue() : result);
        } else if (operator == Operator.SUBTRACT && simpleLeft.equals(simpleRight)) {
            return new ConstantNode<>(0);
        }
        if (!left.equals(simpleLeft) || !right.equals(simpleRight)) {
            Node<I, C> copy = shallowCopy();
            copy.setChildren(asList(simpleLeft, simpleRight));
            return copy;
        }
        return this;
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
        String left = children.size() > 0? child(0).toString() : "?";
        String right = children.size() > 1? child(1).toString() : "?";
        return format("(%s %s %s)", left, operator, right);
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }
}
