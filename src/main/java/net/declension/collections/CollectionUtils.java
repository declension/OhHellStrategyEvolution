package net.declension.collections;

import net.declension.Validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class CollectionUtils {

    public static <T> T pickRandomly(Random rng, Collection<T> choices) {
        Validation.requireNonEmptyParam(choices, "choices");
        Validation.requireNonNullParam(rng, "Random Number generator");
        T[] choicesArray = (T[]) choices.toArray();
        return choicesArray[rng.nextInt(choicesArray.length)];
    }

    public static <T> Collection<T> tentativeCollection(Collection<T> current, T additional) {
        Collection<T> ret = new ArrayList<>(current);
        ret.add(additional);
        return ret;
    }

    public static int totalOf(Collection<Integer> bids) {
        return bids.stream().mapToInt(Integer::intValue).sum();
    }

    public static boolean containsNoNulls(Collection<?> items) {
        return items != null && items.stream().noneMatch(v -> v == null);
    }
}
