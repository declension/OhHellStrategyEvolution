package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.NodeFactory;
import net.declension.games.cards.ohhell.GameSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

import static net.declension.utils.Validation.requireNonNullParam;

/**
 * Creates genetic strategies with random bid-evaluation trees.
 */
public class GeneticStrategyFactory extends AbstractCandidateFactory<GeneticStrategy> {
    public static final int MIN_DEPTH = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticStrategyFactory.class);
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
        // TODO: proper, parameterised initialisation of tree!
        Node<Range, BidEvaluationContext> randomTree = bidNodeFactory.createRandomTree(MIN_DEPTH, maxDepth);
        LOGGER.debug("Creating new {} with bid evaluator of {}", GeneticStrategy.class.getSimpleName(), randomTree);
        return new GeneticStrategy(gameSetup, randomTree);
    }
}
