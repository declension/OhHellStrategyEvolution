package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.strategy.bidding.RandomBiddingStrategy;
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;

import java.util.Random;

public class RandomStrategy implements OhHellStrategy, RandomBiddingStrategy, RandomPlayingStrategy {
    static final String NAME = "RND|RND";
    protected transient final Random rng;

    public RandomStrategy(Random rng) {
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
