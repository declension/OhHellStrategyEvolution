package net.declension.games.cards.sorting;

import net.declension.games.cards.Rank;

import java.util.Comparator;

/**
 * Note this depends explicitly on the ordering of {@link net.declension.games.cards.Rank}.
 */
public class AceHighRankComparator implements Comparator<Rank> {
    @Override
    public int compare(Rank left, Rank right) {
        return Integer.compare(left.ordinal(), right.ordinal());
    }
}
