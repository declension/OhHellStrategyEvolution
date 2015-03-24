package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.strategy.bidding.AverageBiddingStrategy;
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;

import java.util.Random;

public class AverageRandomStrategy implements OhHellStrategy, RandomPlayingStrategy, AverageBiddingStrategy {
    static final String NAME = "AVG|RND";
    private final Random rng;

    public AverageRandomStrategy(Random rng) {
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
