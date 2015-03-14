package net.declension.games.cards;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.ansi;

public class Card implements PrettyPrintable {
    private final Suit suit;
    private final Rank rank;

    public Card(Rank rank, Suit suit) {
        if (suit == null) {
            throw new IllegalArgumentException("Suit cannot be null");
        }
        if (rank == null) {
            throw new IllegalArgumentException("Rank cannot be null");
        }
        this.suit = suit;
        this.rank = rank;
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
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
}
