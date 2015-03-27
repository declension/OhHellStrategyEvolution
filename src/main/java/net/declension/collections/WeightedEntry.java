package net.declension.collections;

import java.util.Map;

import static java.lang.String.format;
import static net.declension.utils.Validation.requireNonNullParam;

public class WeightedEntry<V> implements Map.Entry<Integer, V> {
    final int weight;
    final V value;

    private WeightedEntry(Integer weight, V value) {
        requireNonNullParam(weight, "Weight");
        requireNonNullParam(value, "Value");
        this.weight = weight;
        this.value = value;
    }

    public static <T> WeightedEntry<T> weightedEntry(Integer weight, T value) {
        return new WeightedEntry<>(weight, value);
    }

    public int weight() {
        return weight;
    }

    @Override
    public Integer getKey() {
        return weight;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("Immutable entry");
    }

    @Override
    public String toString() {
        return format("%s (%d)", value, weight);
    }
}
