package net.declension.ea.cards.ohhell.nodes;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;


public class AggregatingNode<I, T> extends Node<I, T> {

    private final Aggregator aggregator;

    public AggregatingNode(Aggregator aggregator) {
        requireNonNull(aggregator);
        this.aggregator = aggregator;
    }

    public AggregatingNode(Aggregator aggregator, Node<I, T>...children) {
        this(aggregator);
        this.children = asList(children);
    }

    public static <I, T> AggregatingNode<I,T> aggregator(Aggregator aggregator) {
        return new AggregatingNode<>(aggregator);
    }

    enum Aggregator {
        COUNT("count", (l, c) -> l.size()),
        MIN("min", (l, c) -> l.stream().min(c).orElse(Double.MIN_VALUE)),
        MAX("max", (l, c) -> l.stream().max(c).orElse(Double.MAX_VALUE)),
        SUM("sum", (l, c) -> l.stream().mapToDouble(Number::doubleValue).sum()),
        MEAN("avg", (l, c) -> l.stream().mapToDouble(Number::doubleValue).average().orElse(0)),
        VARIANCE("var", (l, c) -> {
            double mean = l.stream().mapToDouble(Number::doubleValue).average().orElse(0);
            return l.stream()
                    .mapToDouble(v -> (v.doubleValue() - mean) * (v.doubleValue() - mean))
                    .sum();
        }),
        ;
        public static final List<Aggregator> ALL_AGGREGATORS = asList(values());

        private final String symbol;
        private final BiFunction<Collection<Number>, Comparator<Number>, Number> operator;

        Aggregator(String symbol, BiFunction<Collection<Number>, Comparator<Number>, Number>
                unaryOperator) {
            this.symbol = symbol;
            operator = unaryOperator;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public <T> Number apply(Collection<Number> numbers, Comparator<Number> comparator) {
            return operator.apply(numbers, comparator);
        }
    }


    @Override
    public Number doEvaluation(I item, T context) {
        List<Number> values = children().stream()
                                        .map(n -> n.evaluate(item,context))
                                        .collect(toList());
        return aggregator.apply(values, getComparator());
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
