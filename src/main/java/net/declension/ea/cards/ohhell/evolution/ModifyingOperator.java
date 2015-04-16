package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.slf4j.Logger;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

import static java.lang.Integer.toHexString;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class ModifyingOperator implements EvolutionaryOperator<GeneticStrategy> {
    private static final int AVERAGE_NODE_COUNT = 5;
    protected final Logger logger = getLogger(getClass());

    protected final Probability individualModificationProbability;
    protected final Probability nodeModificationProbability;
    protected Random rng;

    /**
     *
     * @param individualModificationProbability probability of an individual strategy being mutated at all
     * @param nodeModificationProbability probability for each node in the "genome" being mutated.
     */
    public ModifyingOperator(Probability individualModificationProbability, Probability nodeModificationProbability) {
        this.individualModificationProbability = individualModificationProbability;
        this.nodeModificationProbability = nodeModificationProbability;
    }

    @Override
    public List<GeneticStrategy> apply(List<GeneticStrategy> selectedCandidates, Random rng) {
        this.rng = rng;
        return selectedCandidates.stream()
                                 .map(this::modifyStrategy)
                                 .collect(toList());
    }

    protected GeneticStrategy modifyStrategy(GeneticStrategy strategy) {
        if (individualModificationProbability.nextEvent(rng)) {
            GeneticStrategy newStrategy = new GeneticStrategy(strategy);
            Node<Range, BidEvaluationContext> modifiedNode = newStrategy.getBidEvaluator();
            // Arbitrary scaling, for now.
            Probability adjustedProbability = new Probability(
                    min(1, nodeModificationProbability.doubleValue() * AVERAGE_NODE_COUNT / modifiedNode.countNodes()));
            modifiedNode.accept(node -> {
                if (adjustedProbability.nextEvent(rng)) {
                    modifyNode(node);
                }
            });
            if (modifiedNode.equals(strategy.getBidEvaluator())) {
                return strategy;
            }
            Node<Range, BidEvaluationContext> bidNode = strategy.getBidEvaluator();
            logger.debug("Modified bid evaluator #{} -> #{}",
                         toHexString(bidNode.hashCode()), toHexString(modifiedNode.hashCode()));
            return newStrategy;
        }
        return strategy;
    }

    protected abstract void modifyNode(Node<Range, BidEvaluationContext> node);
}
