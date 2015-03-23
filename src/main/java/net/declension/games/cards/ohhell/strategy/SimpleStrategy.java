package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.strategy.bidding.AverageBidStrategy;
import net.declension.games.cards.ohhell.strategy.playing.SimplePlayingStrategy;

public class SimpleStrategy implements Strategy, AverageBidStrategy, SimplePlayingStrategy {
    static final String NAME = "AVG|SIM";
    private final GameSetup gameSetup;

    public SimpleStrategy(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public GameSetup getGameSetup() {
        return gameSetup;
    }
}
