package net.declension.collections;

import net.declension.Validation;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class CollectionUtils {

    public static final <K> Comparator<Map.Entry<K, Integer>> compareEntriesByDescendingValues() {
        return comparing(Map.Entry::getValue, (l, r) -> Integer.compare(r, l));
    }

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

    public static <T> List<Map.Entry<T, Integer>> rankEntrySetByIntValue(Collection<Map.Entry<T, Integer>> input) {
        List<Map.Entry<T, Integer>> list = input.stream().sequential()
                                                .sorted(comparing(Map.Entry::getValue, Integer::compare))
                                                .collect(toList());

        List<Map.Entry<T, Integer>> rankings = new ArrayList<>();
        for (int i = 1; i < list.size() + 1; i++) {
            rankings.add(new AbstractMap.SimpleImmutableEntry<>(list.get(list.size() - i).getKey(), i));
        }
        return rankings;
    }
}
