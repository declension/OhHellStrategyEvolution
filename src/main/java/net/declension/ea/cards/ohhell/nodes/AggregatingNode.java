package net.declension.ea.cards.ohhell.nodes;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class AggregatingNode<I, T> extends Node<I, T> {

    private final Aggregator aggregator;

    public AggregatingNode(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public AggregatingNode(Aggregator aggregator, Node<I, T>...children) {
        this.aggregator = aggregator;
        this.children = asList(children);
    }

    enum Aggregator {
        COUNT("count", (l, c) -> l.size()),
        MIN("min", (l, c) -> l.stream().min(c).get()),
        MAX("max", (l, c) -> l.stream().max(c).get()),
        SUM("sum", (l, c) -> l.stream().mapToDouble(Number::doubleValue).sum()),
        MEAN("avg", (l, c) -> l.stream().mapToDouble(Number::doubleValue).average().getAsDouble()),
        VARIANCE("var", (l, c) -> {
            double mean = l.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
            return l.stream()
                    .mapToDouble(v -> (v.doubleValue() - mean) * (v.doubleValue() - mean))
                    .sum();
        }),
        ;

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
}
