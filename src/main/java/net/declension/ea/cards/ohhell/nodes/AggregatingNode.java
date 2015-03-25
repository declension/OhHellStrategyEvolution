package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.ListNumber;

import java.util.*;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.data.SingleItemListNumber.singleItemOf;

public class AggregatingNode<T> extends Node<T> {

    private final Aggregator aggregator;

    public AggregatingNode(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public AggregatingNode(Aggregator aggregator, Node<T>...children) {
        this.aggregator = aggregator;
        this.children = asList(children);
    }

    public Comparator<ListNumber> getComparator() {
        return Comparator.comparing(ListNumber::doubleValue);
    }


    enum Aggregator {
        COUNT("count", (l, c) -> l.size()),
        MIN("min", (l, c) -> l.stream().min(c).get()),
        MAX("max", (l, c) -> l.stream().max(c).get()),
        SUM("sum", (l, c) -> l.stream().mapToDouble(ListNumber::doubleValue).sum()),
        MEAN("avg", (l, c) -> l.stream().mapToDouble(ListNumber::doubleValue).average().getAsDouble()),
        VARIANCE("var", (l, c) -> {
            double mean = l.stream().mapToDouble(ListNumber::doubleValue).average().getAsDouble();
            return l.stream()
                    .mapToDouble(v -> (v.doubleValue() - mean) * (v.doubleValue() - mean))
                    .sum();
        }),
        ;


        private final String symbol;
        private final BiFunction<Collection<ListNumber>, Comparator<ListNumber>, Number> operator;

        Aggregator(String symbol, BiFunction<Collection<ListNumber>, Comparator<ListNumber>, Number>
                unaryOperator) {
            this.symbol = symbol;
            this.operator = unaryOperator;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public <T> ListNumber apply(Collection<ListNumber> numbers, Comparator<ListNumber> comparator) {
            return singleItemOf(operator.apply(numbers, comparator));
        }
    }


    @Override
    public ListNumber evaluate(T context) {
        List<ListNumber> values = children().stream()
                                            .map(n -> n.evaluate(context))
                                            .collect(toList());
        return aggregator.apply(values, getComparator());
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
