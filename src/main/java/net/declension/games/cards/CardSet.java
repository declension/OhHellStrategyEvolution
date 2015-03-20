package net.declension.games.cards;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import static net.declension.Validation.requireNonNullParam;

/**
 * Models a set of card, typically held by someone, that is kept in order.
 */
public class CardSet extends TreeSet<Card> {

    public CardSet(Comparator<Card> cardOrdering) {
        super(cardOrdering);
    }

    /**
     * Use the (sensible) default ordering, with trumps as supplied, and initialise with the cards in {@code cards}.
     */
    public CardSet(Comparator<Card> cardOrdering, Collection<Card> cards) {
        super(cardOrdering);
        requireNonNullParam(cards, "Cards collection");
        addAll(cards);
    }

    public Card getHighest() {
        return this.last();
    }

    public Card getLowest() {
        return this.first();
    }
}
