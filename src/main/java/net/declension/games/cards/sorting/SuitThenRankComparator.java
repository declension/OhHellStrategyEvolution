package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;

import java.util.Comparator;

import static net.declension.utils.Validation.requireNonNullParam;

public class SuitThenRankComparator implements Comparator<Card> {

    private final Comparator<Card> comparator;

    public SuitThenRankComparator(Comparator<Suit> suitComparator, Comparator<Rank> rankComparator) {
        requireNonNullParam(rankComparator, "Rank Comparator");
        requireNonNullParam(suitComparator, "Suit Comparator");
        comparator = Comparator.comparing(Card::suit, suitComparator)
                               .thenComparing(Card::rank, rankComparator);
    }

    @Override
    public int compare(Card left, Card right) {
        return comparator.compare(left, right);
    }
}
