package net.declension.collections;

import net.declension.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class CollectionUtils {

    public static <T> T pickRandomly(Random rng, Collection<T> choices) {
        Utils.requireNonEmptyParam(choices, "choices");
        Utils.requireNonNullParam(rng, "Random Number generator");
        T[] choicesArray = (T[]) choices.toArray();
        return choicesArray[rng.nextInt(choicesArray.length)];
    }

    public static <T> Collection<T> tentativeCollection(Collection<T> current, T additional) {
        Collection<T> ret = new ArrayList<>(current);
        ret.add(additional);
        return ret;
    }

}
