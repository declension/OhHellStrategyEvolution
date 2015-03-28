package net.declension.ea.cards.ohhell.nodes;

import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.asList;

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
        LOG("log", Math::log10),
        //SIN("sin", Math::sin),
        FLOOR("floor", Math::floor),
        ABS("abs", Math::abs),
        //SIGNUM("sgn", Math::signum)
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

    @Override
    public String toString() {
        return format("%s(%s)", operator, child(0));
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(1);
    }
}
