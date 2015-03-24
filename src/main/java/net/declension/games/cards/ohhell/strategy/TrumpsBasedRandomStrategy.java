package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.strategy.bidding.TrumpsBasedBiddingStrategy;
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;

import java.util.Random;

public class TrumpsBasedRandomStrategy implements OhHellStrategy, TrumpsBasedBiddingStrategy, RandomPlayingStrategy {
    private final Random rng;

    public TrumpsBasedRandomStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public String getName() {
        return "TRP|RND";
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
