package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.suit.TrumpsOrNothingSuitComparator;

import java.util.Comparator;

import static net.declension.Validation.requireNonNullParam;

public class TrumpsSuitThenRankCardComparator implements Comparator<Card> {
    private Comparator<Card> comparing;

    public TrumpsSuitThenRankCardComparator(Comparator<Rank> rankComparator, Suit trumps) {
        requireNonNullParam(rankComparator, "Rank Comparator");
        comparing = Comparator.comparing(Card::suit, new TrumpsOrNothingSuitComparator(trumps))
                              .thenComparing(Card::rank, rankComparator);
    }

    @Override
    public int compare(Card left, Card right) {
        return comparing.compare(left, right);
    }

}
