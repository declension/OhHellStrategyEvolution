package net.declension.ea.cards.ohhell;

import static java.lang.String.format;

public class Range extends Number implements Comparable<Number> {
    final int value;
    private final int min;
    private final int max;

    public Range(int value, int min, int max) {
        this.value = value;
        this.min = min;
        this.max = max;
        if (value > max || value < min) {
            throw new IllegalArgumentException(format("%d not within allowed range: (%d, %d)", value, min, max));
        }
    }

    @Override
    public int compareTo(Number other) {
        return other == null? 0 : Long.compare(value, other.longValue());
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    @Override
    public String toString() {
        return format("{%s|%s|%s}", min, value, max);
    }
}
