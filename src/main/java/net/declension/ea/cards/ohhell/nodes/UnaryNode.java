package net.declension.ea.cards.ohhell.nodes;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;

public class UnaryNode<I, C> extends Node<I, C> {
    private final Operator operator;

    protected UnaryNode(Operator operator) {
        this.operator = operator;
    }

    protected UnaryNode(Operator operator, Node<I, C> left, Node<I, C> right) {
        this.operator = operator;
        children = asList(left, right);
    }

    public static <I, C> UnaryNode<I, C> unary(Operator op) {
        return new UnaryNode<>(op);
    }

    @Override
    public Number doEvaluation(I item, C context) {
        return compute(child(0), item, context);
    }

    protected Number compute(Node<I, C> onlyChild, I item, C context) {
        Number leftVal = onlyChild.evaluate(item, context);
        return operator.apply(leftVal);
    }

    enum Operator {
        LN("ln", x -> x==0? Double.POSITIVE_INFINITY : Math.log(x)),
        FLOOR("floor", Math::floor),
        ABS("abs", Math::abs),
        ;
        public static final List<Operator> ALL_UNARY_OPERATORS = asList(Operator.values());

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

    public Operator getOperator() {
        return operator;
    }

    @Override
    public <T extends Node<I, C>> T mutatedCopy(Random rng) {
        T mutant = (T) new UnaryNode<>(pickRandomEnum(rng, Operator.class));
        mutant.setChildren(children);
        return mutant;
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
