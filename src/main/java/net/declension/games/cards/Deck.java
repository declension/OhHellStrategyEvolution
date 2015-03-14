package net.declension.games.cards;

import com.google.common.collect.ImmutableList;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static net.declension.games.cards.Rank.ALL_RANKS;
import static net.declension.games.cards.Suit.ALL_SUITS;

public class Deck implements Iterable<Card> {
    private final Deque<Card> cards;

    /**
     * A full, ordered deck.
     */
    public Deck() {
        this(getAllCards());
    }

    /**
     * Set up from a copy of a collection of cards;
     * @param cards incoming cards
     */
    public Deck(Collection<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards cannot be null or empty");
        }
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

    public Card topCard() {
        return cards.pop();
    }



    public boolean hasCards() {
        return !cards.isEmpty();
    }

    private static List<Card> getAllCards() {
        return ALL_RANKS.stream()
                .flatMap(rank -> ALL_SUITS.stream().map(suit -> new Card(rank, suit)))
                .collect(toList())
        ;
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
        return "Deck " + cards;
    }

    public int size() {
        return cards.size();
    }
}
