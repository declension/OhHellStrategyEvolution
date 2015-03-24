package net.declension.ea.cards.ohhell;

import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import net.declension.games.cards.ohhell.strategy.RandomStrategy;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class OhHellStrategyCandidateFactory extends AbstractCandidateFactory<OhHellStrategy> {
    int total = 0;

    @Override
    public OhHellStrategy generateRandomCandidate(Random rng) {
        total++;
        return new RandomStrategy(rng);
    }
}
