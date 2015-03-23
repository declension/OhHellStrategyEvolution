package net.declension.games.cards;

import com.google.common.collect.ImmutableList;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static net.declension.utils.Validation.requireNonNullParam;

public class Deck implements Iterable<Card> {
    private final Deque<Card> cards;

    /**
     * A full, ordered deck.
     */
    public Deck() {
        this(Card.allPossibleCards());
    }

    /**
     * Set up from a copy of a collection of cards;
     * @param cards incoming cards
     */
    public Deck(Collection<Card> cards) {
        requireNonNullParam(cards, "Cards");
        this.cards = new LinkedList<>(cards);
    }

    /**
     * Gets an ordered list of the cards, to examine (but not modify).
     * @return an immutable list of cards.
     */
    public ImmutableList<Card> cards() {
        return ImmutableList.copyOf(cards);
    }

    /**
     * Shuffles the current deck, and returns a copy.
     */
    public Deck shuffled() {
        List<Card> newCards = new ArrayList<>(cards);
        Collections.shuffle(newCards);
        return new Deck(newCards);
    }

    public Card pullTopCard() {
        return cards.pop();
    }

    public synchronized List<Card> pullCards(int number) {
        List<Card> pulled = cards.stream()
                .limit(number)
                .collect(toList());
        cards.removeAll(pulled);
        return pulled;
    }

    public boolean hasCards() {
        return !cards.isEmpty();
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deck)) return false;

        Deck other = (Deck) o;

        return cards.equals(other.cards);

    }

    @Override
    public int hashCode() {
        return cards.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Deck with %d cards: %s", cards.size(), cards);
    }

    public int size() {
        return cards.size();
    }
}
