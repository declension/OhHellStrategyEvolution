package net.declension.games.cards;

import net.declension.utils.PrettyPrintable;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static net.declension.Validation.requireNonNullParam;
import static net.declension.games.cards.Rank.ALL_RANKS;
import static net.declension.games.cards.Suit.ALL_SUITS;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Models a traditional(ish) playing card, composed of {@link Suit} and {@link Rank}, and supporting colour printing.
 * A default ordering applies but is not fixed.
 */
public class Card implements PrettyPrintable {
    private final Suit suit;
    private final Rank rank;

    public Card(Rank rank, Suit suit) {
        requireNonNullParam(suit, "Suit cannot be null");
        requireNonNullParam(rank, "Rank cannot be null");
        this.suit = suit;
        this.rank = rank;
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    /**
     * Returns an instance of every possible card (a "full deck").
     * @return a list of cards, in default ordering.
     */
    public static List<Card> allPossibleCards() {
        return ALL_RANKS.stream()
                .flatMap(rank -> ALL_SUITS.stream().map(suit -> new Card(rank, suit)))
                .collect(toList())
        ;
    }

    @Override
    public String prettyString() {
        return ansi().bg(WHITE).fg(suit.isRed() ? RED : BLACK).a(rank).a(suit).reset().toString();
    }

    @Override
    public String toString() {
        //return String.format("%2s%s", rank, suit);
        return prettyString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }

    /**
     * Allows simple comparison of two cards.
     *
     * @param other The card against which to compare this one
     * @return an object which given a comparator will tell you if the current card is better.
     */
    public ComparatorAccepter beats(Card other) {
        return new ComparatorAccepter(other);
    }

    public final class ComparatorAccepter {
        private final Card other;

        public ComparatorAccepter(Card other) {
            this.other = other;
        }

        public boolean using(Comparator<Card> cardComparator) {
            return cardComparator.compare(Card.this, other) == 1;
        }
    }
}
