package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.strategy.bidding.TrumpsBasedBiddingStrategy;
import net.declension.games.cards.ohhell.strategy.playing.SimplePlayingStrategy;

public class TrumpsBasedSimpleStrategy implements OhHellStrategy, TrumpsBasedBiddingStrategy, SimplePlayingStrategy {
    static final String NAME = "TRP|SIM";
    private final GameSetup gameSetup;

    public TrumpsBasedSimpleStrategy(GameSetup gameSetup) {
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
