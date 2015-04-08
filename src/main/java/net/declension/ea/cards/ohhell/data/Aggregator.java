package net.declension.ea.cards.ohhell.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;

public enum Aggregator {
    COUNT("COUNT", 0, (l, c) -> l.size()),
    MIN("MIN", 0, (l, c) -> l.stream().min(c).orElse(Integer.MIN_VALUE)),
    MAX("MAX", 0, (l, c) -> l.stream().max(c).orElse(Integer.MAX_VALUE)),
    SUM("SUM", 0, (l, c) -> l.stream().mapToDouble(Number::doubleValue).sum()),
    MEAN("AVG", 0, (l, c) -> l.stream().mapToDouble(Number::doubleValue).average().orElse(0)),
    VARIANCE("VAR", 0, (l, c) -> {
        double mean = l.stream().mapToDouble(Number::doubleValue).average().orElse(0);
        return l.stream()
                .mapToDouble(v -> (v.doubleValue() - mean) * (v.doubleValue() - mean))
                .sum();
    }),
    ;
    public static final List<Aggregator> ALL_AGGREGATORS = asList(Aggregator.values());
    public static final Comparator<Number> DOUBLE_COMPARATOR = comparing(Number::doubleValue);

    private final String symbol;
    private final Number identity;
    private final BiFunction<Collection<Number>, Comparator<Number>, Number> operator;

    Aggregator(String symbol, Number identity,
               BiFunction<Collection<Number>, Comparator<Number>, Number> unaryOperator) {
        this.symbol = symbol;
        this.identity = identity;
        operator = unaryOperator;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public Number apply(Collection<Number> numbers, Comparator<Number> comparator) {
        return operator.apply(numbers, comparator);
    }

    public Number apply(Collection<Number> numbers) {
        return apply(numbers, DOUBLE_COMPARATOR);
    }

    public Number identityValue() {
        return identity;
    }
}
