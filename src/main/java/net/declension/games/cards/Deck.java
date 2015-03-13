package net.declension.games.cards;

import com.google.common.collect.ImmutableList;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Deck implements Iterable<Card> {
    private final List<Card> cards;

    /**
     * A full, ordered deck.
     */
    public Deck() {
        this(getAllCards());
    }

    /**
     * Set up from a copy of a collection of cards;
     * @param cards
     */
    public Deck(Collection<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards cannot be null or empty");
        }
        this.cards = new ArrayList<>(cards);
    }

    public ImmutableList<Card> cards() {
        return ImmutableList.copyOf(cards);
    }

    /**
     * Shuffles the current deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public ImmutableList<List<Card>> shuffled() {
        List<Card> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        return ImmutableList.of(shuffled);
    }

    private static List<Card> getAllCards() {
        return asList(Rank.values())
                .stream()
                .flatMap(rank -> Arrays.asList(Suit.values()).stream().map(suit -> new Card(rank, suit)))
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
}
