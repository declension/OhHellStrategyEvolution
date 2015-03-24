package net.declension.games.cards.ohhell;

public interface NewGameListener {

    /**
     * Called when a new game is set up.
     * @param game the new game
     */
    void onNewGame(Game game);
}
