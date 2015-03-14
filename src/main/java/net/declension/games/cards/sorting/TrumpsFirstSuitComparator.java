package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsFirstSuitComparator implements Comparator<Suit> {

    private final Suit trumps;

    public TrumpsFirstSuitComparator(Suit trumps) {
        this.trumps = trumps;
    }

    @Override
    public int compare(Suit o1, Suit o2) {
        int trumpDiff = Boolean.compare(trumps.equals(o1), trumps.equals(o2));
        return trumpDiff == 0? Integer.compare(o1.ordinal(), o2.ordinal()) : trumpDiff;
    }
}
