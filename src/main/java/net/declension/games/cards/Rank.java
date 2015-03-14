package net.declension.games.cards;

import java.util.List;

import static java.util.Arrays.asList;

public enum Rank {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A"),
    ;

    public static final List<Rank> ALL_RANKS = asList(values());
    private final String defaultString;

    Rank(String defaultString) {
        this.defaultString = defaultString;
    }

    @Override
    public String toString() {
        return String.format("%2s", defaultString);
    }

}
