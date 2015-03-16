package net.declension.games.cards.sorting;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsAwareSuitsFirstComparator implements Comparator<Card> {
    private final Comparator<Suit> suitComparator;
    private final Comparator<Rank> rankComparator;

    public TrumpsAwareSuitsFirstComparator(Comparator<Rank> rankComparator, Suit trumps) {
        this.rankComparator = rankComparator;
        suitComparator = trumps == null? new EnumComparator<>() : new TrumpsFirstSuitComparator(trumps);
    }

    @Override
    public int compare(Card left, Card right) {
        int suitsValue = suitComparator.compare(left.suit(), right.suit());
        return suitsValue == 0 ? rankComparator.compare(left.rank(), right.rank()) : suitsValue;
    }
}
