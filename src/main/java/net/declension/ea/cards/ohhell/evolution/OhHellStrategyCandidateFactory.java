package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.RandomNode;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class OhHellStrategyCandidateFactory extends AbstractCandidateFactory<OhHellStrategy> {
    int total = 0;
    private final GameSetup gameSetup;

    public OhHellStrategyCandidateFactory(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    @Override
    public OhHellStrategy generateRandomCandidate(Random rng) {
        total++;
        // TODO: proper initialisation of tree!
        RandomNode<Range, BidEvaluationContext> rootBiddingNode = new RandomNode<>(gameSetup.getRNG());
        return new GeneticStrategy(gameSetup, rootBiddingNode);
    }
}
