package net.declension.games.cards;

import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.sorting.TrumpsAwareSuitsFirstComparator;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Models a set of card, typically held by someone.
 */
public class CardSet extends TreeSet<Card> {

    /**
     * Use the (sensible) default ordering, with trumps as supplied.
     * @param trumps the current trumps
     */
    public CardSet(Suit trumps) {
        this(new TrumpsAwareSuitsFirstComparator(new AceHighRankComparator(), trumps));
    }

    public CardSet(Comparator<Card> cardOrdering) {
        super(cardOrdering);
    }

}