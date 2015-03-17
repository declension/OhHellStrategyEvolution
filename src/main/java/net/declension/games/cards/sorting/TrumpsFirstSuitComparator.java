package net.declension.games.cards.sorting;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsFirstSuitComparator implements Comparator<Suit> {
    private final Comparator<Suit> comparator;

    public TrumpsFirstSuitComparator(Suit trumps) {
        comparator = new TrumpsOrNothingSuitComparator(trumps).thenComparing(new EnumComparator<>());
    }

    @Override
    public int compare(Suit o1, Suit o2) {
        return comparator.compare(o1, o2);
    }
}
