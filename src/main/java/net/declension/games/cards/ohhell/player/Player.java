package net.declension.games.cards.ohhell.player;


import net.declension.games.cards.Card;
import net.declension.games.cards.EndGameListener;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.NewGameListener;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.Map;

public interface Player extends NewHandListener, NewGameListener, EndGameListener {

    PlayerID getID();

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Trick trickSoFar);

    OhHellStrategy getStrategy();

    boolean hasCards();

    @Override
    default void onNewGame(Game game) {
        // Empty
    }

    @Override
    default void onGameEnd(Map<Player, Integer> scores) {
        // empty
    }
}
