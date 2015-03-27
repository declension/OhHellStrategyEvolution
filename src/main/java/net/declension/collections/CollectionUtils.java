package net.declension.collections;

import net.declension.utils.Validation;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public final class CollectionUtils {

    private CollectionUtils() {
        // Empty
    }

    public static <K> Comparator<Map.Entry<K, Integer>> comparingEntriesByDescendingValues() {
        return comparing(Map.Entry::getValue, (l, r) -> Integer.compare(r, l));
    }

    public static <T> T pickRandomly(Random rng, Collection<T> choices) {
        Validation.requireNonEmptyParam(choices, "choices");
        Validation.requireNonNullParam(rng, "Random Number generator");
        T[] choicesArray = (T[]) choices.toArray();
        return choicesArray[rng.nextInt(choicesArray.length)];
    }

    public static <T extends Enum<T>> T pickRandomEnum(Random rng, Class<? extends Enum<T>> enumClass) {
        Validation.requireNonNullParam(rng, "Random Number generator");
        Validation.requireNonNullParam(enumClass, "enum");
        // TODO: make this enum Generics madness neater, somehow.
        T[] choicesArray = (T[]) enumClass.getEnumConstants();
        if (choicesArray == null) {
            throw new IllegalArgumentException("Enum " + enumClass.getSimpleName() + " doesn't have values");
        }
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

    /**
     * Does <i>ordinal</i> ranking on Integer values, with a random element for ties (to ensure fairness).
     *
     * @param input a collection of {@link Map.Entry} values to rank <strong>by their values</strong>.
     * @param <K> the key type
     * @return an ordered List of new entries representing the original data.
     */
    public static <K> List<Map.Entry<K, Integer>> rankEntrySetByIntValue(Collection<Map.Entry<K, Integer>> input) {
        List<Map.Entry<K, Integer>> list = input.stream().sequential()
                                                .sorted(randomisedValueSorting())
                                                .collect(toList());

        List<Map.Entry<K, Integer>> rankings = new ArrayList<>();
        for (int i = 1; i < list.size() + 1; i++) {
            rankings.add(new AbstractMap.SimpleImmutableEntry<>(list.get(list.size() - i).getKey(), i));
        }
        return rankings;
    }

    private static <K> Comparator<Map.Entry<K, Integer>> randomisedValueSorting() {
        Random rng = new Random();
        return (l, r) -> {
            int realOrder = Integer.compare(l.getValue(), r.getValue());
            return realOrder == 0? rng.nextInt(3) - 1 : realOrder;
        };
    }


    public static <T> Optional<T> lowestAbove(Collection<T> items, Comparator<T> order, T referenceItem) {
        return firstThat(items, item -> order.compare(item, referenceItem) == 1, order);
    }

    public static <T> Optional<T> highestBelow(Collection<T> items, Comparator<T> order, T referenceItem) {
        return firstThat(items, item -> order.compare(item, referenceItem) == -1, order.reversed());
    }

    public static <T> Optional<T> firstThat(Collection<T> items, Predicate<T> filter, Comparator<T> order) {
        return items.stream()
                    .filter(filter)
                    .sorted(order)
                    .findFirst();
    }
}
