package net.declension.ea.cards.ohhell.nodes;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;

public class UnaryNode<I, C> extends Node<I, C> {
    private Operator operator;

    public enum Operator {
        LN("ln", x -> {if (x < 0) return Double.NaN; return x==0? Double.POSITIVE_INFINITY : Math.log(x);}, false),
        FLOOR("floor", Math::floor, true),
        ABS("abs", Math::abs, true),
        ;
        public static final List<Operator> ALL_UNARY_OPERATORS = asList(Operator.values());

        private final String symbol;
        private final DoubleUnaryOperator doubleOperator;
        private final boolean idempotent;

        Operator(String symbol, DoubleUnaryOperator doubleOperator, boolean idempotent) {
            this.symbol = symbol;
            this.doubleOperator = doubleOperator;
            this.idempotent = idempotent;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public Number apply(Number leftArg) {
            Double result = doubleOperator.applyAsDouble(leftArg.doubleValue());
            if (leftArg instanceof Integer && this == FLOOR || this == ABS) {
                return result.intValue();
            }
            return result;
        }

        public boolean isIdempotent() {
            return idempotent;
        }
    }

    protected UnaryNode(Operator operator) {
        this.operator = operator;
    }

    public static <I, C> UnaryNode<I, C> unary(Operator op) {
        return new UnaryNode<>(op);
    }

    public static <I, C> UnaryNode<I, C> unary(Operator op, Node<I,C> child) {
        UnaryNode<I, C> ret = new UnaryNode<>(op);
        ret.addChild(child);
        return ret;
    }

    @Override
    public Node<I,C> simplifiedVersion() {
        Node<I, C> child = children.get(0).simplifiedVersion();
        if (child instanceof ConstantNode) {
            Number childValue = ((ConstantNode) child).getValue();
            Number applied = operator.apply(childValue);
            if (childValue instanceof Integer) {
                new ConstantNode<>(applied.intValue());
            }
            return new ConstantNode<>(applied);
        }
        if (child instanceof UnaryNode) {
            Operator childOp = ((UnaryNode) child).getOperator();
            if (operator == childOp && (operator.isIdempotent())) {
                return child;
            }
        }
        return this;
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new UnaryNode<>(operator);
    }

    @Override
    public Number doEvaluation(I item, C context) {
        return compute(child(0), item, context);
    }

    protected Number compute(Node<I, C> onlyChild, I item, C context) {
        Number leftVal = onlyChild.evaluate(item, context);
        return operator.apply(leftVal);
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public Node<I,C> mutate(Random rng) {
        if (Operator.ALL_UNARY_OPERATORS.size() == 1) {
            // Nothing to mutate to..
            return this;
        }
        Operator newOp;
        do {
            newOp = pickRandomEnum(rng, Operator.class);
        } while (newOp == operator);
        logger.debug("Mutating {}: {} -> {}", this, operator, newOp);
        operator = newOp;
        return this;
    }

    @Override
    public String toString() {
        return format("%s(%s)", operator, child(0));
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(1);
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

        UnaryNode unaryNode = (UnaryNode) other;
        return operator == unaryNode.operator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
