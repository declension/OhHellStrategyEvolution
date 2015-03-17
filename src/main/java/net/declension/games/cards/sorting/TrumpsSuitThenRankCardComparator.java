package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsSuitThenRankCardComparator implements Comparator<Card> {
    private final Comparator<Rank> rankComparator;
    private final Suit trumps;

    public TrumpsSuitThenRankCardComparator(Comparator<Rank> rankComparator, Suit trumps) {
        this.rankComparator = rankComparator;
        this.trumps = trumps;
    }

    @Override
    public int compare(Card left, Card right) {
        Comparator<Card> comparing = Comparator.comparing(Card::suit, new TrumpsOrNothingSuitComparator(trumps))
                .thenComparing(Card::rank, rankComparator);
        return comparing.compare(left, right);
    }

}
