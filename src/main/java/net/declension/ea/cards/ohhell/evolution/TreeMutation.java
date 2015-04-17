package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class TreeMutation implements EvolutionaryOperator<GeneticStrategy> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeMutation.class);
    protected final Logger logger = getLogger(getClass());
    private final NodeFactory<Range, BidEvaluationContext> bidNodeFactory;
    protected Random rng;

    /**
     * @param nodeModificationProbability   probability for each node in the "genome" being mutated.
     * @param bidNodeFactory
     */
    public TreeMutation(Probability nodeModificationProbability,
                        NodeFactory<Range, BidEvaluationContext> bidNodeFactory) {
        this.bidNodeFactory = bidNodeFactory;
    }

    @Override
    public List<GeneticStrategy> apply(List<GeneticStrategy> selectedCandidates, Random rng) {
        this.rng = rng;
        return selectedCandidates.stream()
                                 .map(this::modifyStrategy)
                                 .collect(toList());
    }

    protected GeneticStrategy modifyStrategy(GeneticStrategy strategy) {
        GeneticStrategy newStrategy = new GeneticStrategy(strategy);
        // Arbitrary scaling, for now.
        Node<Range, BidEvaluationContext> bidNode = strategy.getBidEvaluator();

        int index = rng.nextInt(bidNode.countNodes());
        Node<Range, BidEvaluationContext> mutee = bidNode.getNode(index);
        Node<Range, BidEvaluationContext> mutant = mutatedNode(mutee);
        newStrategy.setBidEvaluator(bidNode.copyWithReplacedNode(index, mutant));
        logger.debug("Modified node {} -> {}", mutee, mutant);
        return newStrategy;
    }

    private Node<Range, BidEvaluationContext> mutatedNode(Node<Range, BidEvaluationContext> node) {
        Node<Range, BidEvaluationContext> mutated = node.deepCopy().mutate(rng);
        return mutated.equals(node)? bidNodeFactory.createTerminalNode()
                                   : mutated;
    }
}
