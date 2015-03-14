package net.declension.collections;

import java.util.Collection;
import java.util.Random;

public class CollectionUtils {

    public static <T> T pickRandomly(Random rng, Collection<T> collection) {
        T[] choices = (T[]) collection.toArray();
        return choices[rng.nextInt(choices.length)];
    }
}
