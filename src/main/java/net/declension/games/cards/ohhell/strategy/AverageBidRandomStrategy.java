package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.strategy.bidding.AverageBidStrategy;
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;

import java.util.Random;

public class AverageBidRandomStrategy implements Strategy, RandomPlayingStrategy, AverageBidStrategy {
    static final String NAME = "AVG|RND";
    private final Random rng;

    public AverageBidRandomStrategy(Random rng) {
        this.rng = rng;
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
    public Random getRng() {
        return rng;
    }
}
