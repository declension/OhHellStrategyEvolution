package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;

import java.util.Comparator;

public class SuitThenRankComparator implements Comparator<Card> {
    private final Comparator<Suit> suitComparator;
    private final Comparator<Rank> rankComparator;

    public SuitThenRankComparator(Comparator<Rank> rankComparator, Comparator<Suit> suitComparator) {
        this.rankComparator = rankComparator;
        this.suitComparator = suitComparator;
    }

    @Override
    public int compare(Card left, Card right) {
        int suitsValue = suitComparator.compare(left.suit(), right.suit());
        return suitsValue == 0 ? rankComparator.compare(left.rank(), right.rank()) : suitsValue;
    }
}
