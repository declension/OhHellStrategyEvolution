package net.declension.collections;

import net.declension.Validation;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.Comparator.comparing;

public class CollectionUtils {

    public static <T> T pickRandomly(Random rng, Collection<T> choices) {
        Validation.requireNonEmptyParam(choices, "choices");
        Validation.requireNonNullParam(rng, "Random Number generator");
        T[] choicesArray = (T[]) choices.toArray();
        return choicesArray[rng.nextInt(choicesArray.length)];
    }

    public static int totalOf(Collection<Integer> bids) {
        return bids.stream().mapToInt(Integer::intValue).sum();
    }

    public static int totalOfOptionals(Collection<Optional<Integer>> bids) {
        return bids.stream().mapToInt(opt -> opt.orElse(0)).sum();
    }

    public static boolean containsNoNulls(Collection<?> items) {
        return items != null && items.stream().noneMatch(v -> v == null);
    }

    public static <T> boolean allPresent(Collection<Optional<T>> items) {
        return items != null && items.stream().noneMatch(v -> v == null || !v.isPresent());
    }
    public static final BinaryOperator<Integer> ADD_NULLABLE_INTEGERS
            = (l, r) -> (l == null? 0 : l)  + (r == null? 0 : r);


    static final Function<Double, Double> SQUARE = x -> x * x;

    public static <T> T chooseLowestSquareUsingFunction(Collection<T> values, Function<T, Double> distanceFunction) {
        return values.stream()
                .min(comparing(SQUARE.compose(distanceFunction)))
                .get();
    }
}
