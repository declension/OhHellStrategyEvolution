package net.declension.collections;

import net.declension.Utils;

import java.util.Collection;
import java.util.Random;

public class CollectionUtils {

    public static <T> T pickRandomly(Random rng, Collection<T> choices) {
        Utils.requireNonEmptyParam(choices, "choices");
        Utils.requireNonNullParam(rng, "Random Number generator");
        T[] choicesArray = (T[]) choices.toArray();
        return choicesArray[rng.nextInt(choicesArray.length)];
    }

}
