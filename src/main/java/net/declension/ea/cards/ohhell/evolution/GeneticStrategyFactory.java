package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.NodeFactory;
import net.declension.games.cards.ohhell.GameSetup;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class GeneticStrategyFactory extends AbstractCandidateFactory<GeneticStrategy> {
    int total = 0;
    private final GameSetup gameSetup;
    private final int maxDepth;

    public GeneticStrategyFactory(GameSetup gameSetup, int maxDepth) {
        this.gameSetup = gameSetup;
        this.maxDepth = maxDepth;
    }

    @Override
    public GeneticStrategy generateRandomCandidate(Random rng) {
        total++;
        // TODO: proper initialisation of tree!
        //RandomNode<Range, BidEvaluationContext> rootBiddingNode = new RandomNode<>(gameSetup.getRNG());
        NodeFactory<Range, BidEvaluationContext> bidNodeFactory = new NodeFactory<>(rng);
        return new GeneticStrategy(gameSetup, bidNodeFactory.createRandomTree(maxDepth));
    }
}
