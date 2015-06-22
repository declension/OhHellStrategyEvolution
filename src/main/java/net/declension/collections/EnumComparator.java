package net.declension.collections;

import java.util.Comparator;

public class EnumComparator<T extends Enum<T>> implements Comparator<T> {
    private final Order order;

    /**
     * Default comparator, in ascending order of the enum.
     */
    public EnumComparator() {
        this(Order.NATURAL);
    }

    /**
     * Construct a comparator based on the enum, but of either order.
     * @param order the order in which values should be sorted.
     */
    public EnumComparator(Order order) {
        this.order = order;
    }

    public enum Order {
        NATURAL,
        REVERSED;
    }

    @Override
    public int compare(T left, T right) {
        return order == Order.NATURAL ? Integer.compare(left.ordinal(), right.ordinal())
                                       : Integer.compare(right.ordinal(), left.ordinal());
    }
}
