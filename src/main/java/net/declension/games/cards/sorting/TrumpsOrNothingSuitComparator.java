package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsOrNothingSuitComparator implements Comparator<Suit> {
    private final Suit trumps;

    public TrumpsOrNothingSuitComparator(Suit trumps) {
        this.trumps = trumps;
    }

    @Override
    public int compare(Suit left, Suit right) {
        return Boolean.compare(trumps.equals(left), trumps.equals(right));
    }
}
