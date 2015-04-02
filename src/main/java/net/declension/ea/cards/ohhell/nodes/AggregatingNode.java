package net.declension.ea.cards.ohhell.nodes;

import java.util.*;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;


public class AggregatingNode<I,C> extends Node<I,C> {

    private Aggregator aggregator;

    public AggregatingNode(Aggregator aggregator) {
        requireNonNull(aggregator);
        this.aggregator = aggregator;
    }

    public AggregatingNode(Aggregator aggregator, Node<I, C>...children) {
        this(aggregator);
        this.children = asList(children);
    }

    public static <I,T> AggregatingNode<I,T> aggregator(Aggregator aggregator) {
        return new AggregatingNode<>(aggregator);
    }

    enum Aggregator {
        COUNT("count", 0, (l, c) -> l.size()),
        MIN("min", 0, (l, c) -> l.stream().min(c).orElse(Double.MIN_VALUE)),
        MAX("max", 0, (l, c) -> l.stream().max(c).orElse(Double.MAX_VALUE)),
        SUM("sum", 0, (l, c) -> l.stream().mapToDouble(Number::doubleValue).sum()),
        MEAN("avg", 0, (l, c) -> l.stream().mapToDouble(Number::doubleValue).average().orElse(0)),
        VARIANCE("var", 0, (l, c) -> {
            double mean = l.stream().mapToDouble(Number::doubleValue).average().orElse(0);
            return l.stream()
                    .mapToDouble(v -> (v.doubleValue() - mean) * (v.doubleValue() - mean))
                    .sum();
        }),
        ;
        public static final List<Aggregator> ALL_AGGREGATORS = asList(Aggregator.values());

        private final String symbol;
        private final Number identity;
        private final BiFunction<Collection<Number>, Comparator<Number>, Number> operator;

        Aggregator(String symbol, Number identity,
                   BiFunction<Collection<Number>, Comparator<Number>, Number> unaryOperator
        ) {
            this.symbol = symbol;
            this.identity = identity;
            operator = unaryOperator;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public <T> Number apply(Collection<Number> numbers, Comparator<Number> comparator) {
            return operator.apply(numbers, comparator);
        }

        public Number identityValue() {
            return identity;
        }
    }

    @Override
    public Node<I,C> mutate(Random rng) {

        Aggregator newAgg;
        do {
            newAgg = pickRandomEnum(rng, Aggregator.class);
        } while (Aggregator.ALL_AGGREGATORS.size() > 1 && newAgg == aggregator);
        logger.debug("Mutating {}: {} -> {}", this, aggregator, newAgg);
        aggregator = newAgg;
        return this;
    }

    @Override
    public Node<I, C> simplifiedVersion() {
        if (aggregator == Aggregator.COUNT) {
            // Perhaps that aggregator *is* pointless, but, well, it's got plenty of believable uses.
            return constant(children.size());
        }
        switch (children.size()) {
            case 0:
                return constant(aggregator.identityValue());
            case 1:
                switch (aggregator) {
                    case VARIANCE: return constant(0);
                    default: return child(0);
                }
            default:
                if (children.stream().allMatch(c -> c instanceof ConstantNode)) {
                    return constant(doEvaluation(null, null));
                }
                Node<I, C> simple = shallowCopy();
                simple.setChildren(children.stream()
                                           .map(Node::simplifiedVersion)
                                           .collect(toList()));
                return simple;
        }
    }

    @Override
    public Number doEvaluation(I item, C context) {
        List<Number> values = children().stream()
                                        .map(n -> n.evaluate(item, context))
                                        .collect(toList());
        return aggregator.apply(values, getComparator());
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new AggregatingNode<>(aggregator);
    }


    private Comparator<Number> getComparator() {
        return Comparator.comparing(Number::doubleValue);
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return format("%s(%s)", aggregator, children);
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

        AggregatingNode that = (AggregatingNode) other;
        return aggregator == that.aggregator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregator.hashCode();
        return result;
    }
}
