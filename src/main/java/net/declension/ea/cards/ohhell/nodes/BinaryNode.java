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
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;
import static net.declension.utils.Validation.requireNonNullParam;

public class BinaryNode<I, C> extends Node<I, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryNode.class);
    public static final Node<?, ?> ONE = constant(1);
    public static final Node<?, ?> ZERO = constant(0);
    private Operator operator;

    public enum Operator {
        ADD("+", Double::sum, true),
        SUBTRACT("-", (l, r) -> l - r, false),
        MULTIPLY("*", (l, r) -> l * r, true),
        DIVIDE("/", (l, r) -> l / r, false),
        EXPONENTIATE("^", Math::pow, false);

        static final List<Operator> ALL_BINARY_OPERATORS = asList(values());

        private final String symbol;

        private final DoubleBinaryOperator doubleOperator;
        private final boolean commutative;

        Operator(String symbol, DoubleBinaryOperator doubleOperator, boolean commutative) {
            this.symbol = symbol;
            this.doubleOperator = doubleOperator;
            this.commutative = commutative;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public Number apply(Number leftArg, Number rightArg) {
            return doubleOperator.applyAsDouble(leftArg.doubleValue(), rightArg.doubleValue());
        }

        public boolean isCommutative() {
            return commutative;
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
     * @param operator the operator to use
     * @param left the left node
     * @param right the right node
     */
    public static <I, C> BinaryNode<I, C> binary(Operator operator, Node<I, C> left, Node<I, C> right) {
        BinaryNode<I, C> ret = new BinaryNode<>(operator);
        ret.setChildren(asList(left, right));
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
    protected Node<I, C> shallowCopy() {
        return new BinaryNode<I,C>(operator);
    }

    @Override
    public Node<I, C> mutate(Random rng) {
        if (!operator.isCommutative() && rng.nextInt(2) == 0) {
            swapChildren();
        } else {
            mutateOperator(rng);
        }
        return this;
    }

    private void swapChildren() {
        Node<I, C> firstChild = child(0);
        children.set(0, child(1));
        children.set(1, firstChild);
    }

    private void mutateOperator(Random rng) {
        Operator newOperator;
        do {
            newOperator = pickRandomEnum(rng, Operator.class);
        } while (Operator.ALL_BINARY_OPERATORS.size() > 1 && newOperator == operator);
        LOGGER.debug("Mutating {}: {} -> {}", this, operator, newOperator);
        operator = newOperator;
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
            return reduceToConstant((ConstantNode) simpleLeft, (ConstantNode) simpleRight);
        }
        switch (operator) {
            case ADD:
                if (simpleLeft.equals(constant(0))) {
                    return simpleRight;
                } else if (simpleRight.equals(constant(0))) {
                    return simpleLeft;
                }
                break;
            case SUBTRACT:
                if (simpleLeft.equals(simpleRight)) {
                    return constant(0);
                } else if (simpleRight instanceof ConstantNode && ((ConstantNode) simpleRight).getValue().doubleValue() < 0) {
                    return binary(Operator.ADD, simpleLeft, typeSafeNegate((ConstantNode) simpleRight));
                }
                break;
            case DIVIDE:
                if (simpleLeft.equals(simpleRight)) {
                    return constant(1);
                } else if (simpleRight.equals(ONE)) {
                    return simpleLeft;
                } else if (simpleRight.equals(ZERO)) {
                    return deadNumber();
                } else if (simpleLeft.equals(ZERO)) {
                    return constant(0);
                }
                break;
            case MULTIPLY:
                if (simpleRight.equals(ONE)) {
                    return simpleLeft;
                } else if (simpleLeft.equals(ONE)) {
                    return simpleRight;
                } else if (simpleLeft.equals(ZERO) || simpleRight.equals(ZERO)) {
                    return constant(0);
                }
                break;
            case EXPONENTIATE:
                if (simpleRight.equals(ONE)) {
                    return simpleLeft;
                } else if (simpleRight.equals(ZERO)) {
                    return constant(1);
                } else if (simpleLeft.equals(ONE) || simpleLeft.equals(ZERO)) {
                    return simpleLeft;
                }
                break;
        }
        if (!left.equals(simpleLeft) || !right.equals(simpleRight)) {
            Node<I, C> copy = shallowCopy();
            copy.setChildren(asList(simpleLeft, simpleRight));
            return copy;
        }
        return this;
    }

    private Node<I, C> typeSafeNegate(ConstantNode node) {
        return (node.getValue() instanceof Integer)?
               constant(node.getValue().intValue() * -1)
               : constant(node.getValue().doubleValue() * -1.0);
    }

    private Node<I, C> reduceToConstant(ConstantNode simpleLeft, ConstantNode simpleRight) {
        Number leftResult = simpleLeft.getValue();
        Number rightResult = simpleRight.getValue();
        Number result = operator.apply(leftResult, rightResult);
        return constant(integerFriendly(leftResult, rightResult)? result.intValue() : result);
    }

    private boolean integerFriendly(Number leftResult, Number rightResult) {
        return leftResult instanceof Integer && rightResult instanceof Integer && operator != Operator.DIVIDE;
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
