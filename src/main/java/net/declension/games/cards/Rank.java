package net.declension.games.cards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Rank, String> localisedStrings = new HashMap<>();

    Rank(String defaultString) {
        localisedStrings.put(this, defaultString);
    }


    @Override
    public String toString() {
        return localisedStrings.get(this);
    }


}
