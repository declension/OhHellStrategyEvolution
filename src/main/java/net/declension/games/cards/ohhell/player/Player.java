package net.declension.games.cards.ohhell.player;


import net.declension.games.cards.Card;
import net.declension.games.cards.EndGameListener;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.NewGameListener;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.Map;

public interface Player<T extends OhHellStrategy> extends NewHandListener, NewGameListener, EndGameListener {

    PlayerID getID();

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Trick trickSoFar);

    /**
     * Get the strategy. This is paramaterised on {@link T} to allow implementations to return more specific strategy
     * sub-classes, allowing more interaction than is required by the {@linke OhHellStrategy} (e.g. altering parameters).
     * @return the strategy object for this player.
     */
    T getStrategy();

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
