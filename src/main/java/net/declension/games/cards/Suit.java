package net.declension.games.cards;

import org.fusesource.jansi.Ansi;

import java.util.List;

import static java.util.Arrays.asList;
import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.RED;

public enum Suit implements PrettyPrintable {
    CLUBS("♣"),
    DIAMONDS("♦"),
    HEARTS("♥"),
    SPADES("♠"),
    ;

    public static final List<Suit> ALL_SUITS = asList(values());
    String defaultValue;

    Suit(String defaultSymbol) {
        defaultValue = defaultSymbol;
    }

    @Override
    public String toString() {
        return prettyString();
    }

    @Override
    public String prettyString() {
        return Ansi.ansi().fg(isRed()? RED : BLACK).a(defaultValue).reset().toString();
    }

    protected boolean isRed() {
        return this == DIAMONDS || this == HEARTS;
    }
}
