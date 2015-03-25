package net.declension.ea.cards.ohhell.data;

import java.util.List;

import static java.util.Arrays.asList;
import static net.declension.utils.Validation.requireNonNullParam;

public class SingleItemListNumber extends ListNumber {
    private final List<Number> data;
    private final Number onlyItem;

    public SingleItemListNumber(Number onlyItem) {
        requireNonNullParam(onlyItem, "item");
        this.onlyItem = onlyItem;
        this.data = asList(onlyItem);
    }

    /**
     * Convenience wrapper
     * @param onlyItem
     * @return
     */
    public static SingleItemListNumber singleItemOf(Number onlyItem) {
        return new SingleItemListNumber(onlyItem);
    }

    @Override
    public List<Number> listValue() {
        return data;
    }

    @Override
    public int intValue() {
        return onlyItem.intValue();
    }

    @Override
    public long longValue() {
        return onlyItem.longValue();
    }

    @Override
    public float floatValue() {
        return onlyItem.floatValue();
    }

    @Override
    public double doubleValue() {
        return onlyItem.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Number)) {
            return false;
        }

        Number that = (Number) o;

        return onlyItem.equals(that);

    }

    @Override
    public int hashCode() {
        return onlyItem.hashCode();
    }

    @Override
    public String toString() {
        return onlyItem.toString();
    }
}
