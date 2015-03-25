package net.declension.ea.cards.ohhell.data;

import java.util.List;

/**
 * A very dodgy abstraction of Number.
 * All numbers and lists are made interoperable:
 * Numbers easily become one-item lists
 *
 * Lists should become an average of their members when asked.
 */
public abstract class ListNumber extends Number {

    protected abstract List<Number> listValue();

    /**
     * This should be sub-classed to act appropriately
     */
    public static int compare(ListNumber left, ListNumber right) {
        return Double.compare(left.doubleValue(), right.doubleValue());
    }

}
