package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.NodeFactory;
import net.declension.games.cards.ohhell.GameSetup;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

import static net.declension.utils.Validation.requireNonNullParam;

/**
 * Creates genetic strategies with random bid-evaluation trees.
 */
public class GeneticStrategyFactory extends AbstractCandidateFactory<GeneticStrategy> {
    int total = 0;
    private final GameSetup gameSetup;
    private final int maxDepth;
    private NodeFactory<Range, BidEvaluationContext> bidNodeFactory;

    public GeneticStrategyFactory(GameSetup gameSetup, int maxDepth) {
        requireNonNullParam(gameSetup, "Game Setup");
        this.gameSetup = gameSetup;
        this.maxDepth = maxDepth;
        bidNodeFactory = new NodeFactory<>(gameSetup.getRNG());
    }

    /**
     * Note this implementation ignores the supplied rng, in favour of one set up from the {@link GameSetup}
     */
    @Override
    public GeneticStrategy generateRandomCandidate(Random rng) {
        total++;
        // TODO: proper, parameterised initialisation of tree!
        return new GeneticStrategy(gameSetup, bidNodeFactory.createRandomTree(maxDepth));
    }
}
