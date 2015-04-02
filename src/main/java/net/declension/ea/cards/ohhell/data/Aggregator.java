package net.declension.ea.cards.ohhell.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;

public enum Aggregator {
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
