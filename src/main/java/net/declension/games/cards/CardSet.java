package net.declension.games.cards;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Models a set of card, typically held by someone.
 */
public class CardSet extends TreeSet<Card> {

    /**
     * Use the (sensible) default ordering, with trumps as supplied, and initialise with the cards in {@code cards}.
     */
    public CardSet(Comparator<Card> cardOrdering, Collection<Card> cards) {
        super(cardOrdering);
        this.addAll(cards);
    }

    public CardSet(Comparator<Card> cardOrdering) {
        super(cardOrdering);
    }

}
