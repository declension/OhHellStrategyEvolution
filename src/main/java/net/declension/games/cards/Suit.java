package net.declension.games.cards;

import java.util.List;

import static java.util.Arrays.asList;

public enum Suit {
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
        return defaultValue;
    }
}
