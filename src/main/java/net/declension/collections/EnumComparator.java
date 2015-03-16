package net.declension.collections;

import java.util.Comparator;

public class EnumComparator<T extends Enum<T>> implements Comparator<T> {
    @Override
    public int compare(T left, T right) {
        return Integer.compare(left.ordinal(), right.ordinal());
    }
}
