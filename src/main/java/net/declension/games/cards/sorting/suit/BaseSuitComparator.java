package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;

import java.util.Comparator;

public abstract class BaseSuitComparator implements Comparator<Suit> {

    static final Comparator<Suit> NO_OP_COMPARATOR = (l, r) -> 0;
    protected Comparator<Suit> comparator;

    @Override
    public int compare(Suit left, Suit right) {
        return comparator.compare(left, right);
    }

    protected BaseSuitComparator safelyAppend(Comparator<Suit> other) {
        comparator = comparator == null? other : comparator.thenComparing(other);
        return this;
    }
}
