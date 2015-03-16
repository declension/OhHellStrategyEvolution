package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.TrumpsThenLeadSuitComparator;

import java.util.Comparator;

public class TrickOrderingFactory {

    private final Comparator<Rank> rankComparator;

    public TrickOrderingFactory(Comparator<Rank> rankComparator) {
        this.rankComparator = rankComparator;
    }

    Comparator<Card> create(Suit trumps, Suit lead) {
        return createDefault(trumps, lead);
    }

    private SuitThenRankComparator createDefault(Suit trumps, Suit lead) {
        return new SuitThenRankComparator(rankComparator, new TrumpsThenLeadSuitComparator(trumps, lead));
    }
}
