package net.declension.games.cards.ohhell;

import java.util.List;

@FunctionalInterface
public interface RoundSizer {
    List<Integer> getFor(int numPlayers);
}
