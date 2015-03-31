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
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class NodeMutation implements EvolutionaryOperator<GeneticStrategy> {

    private static final Logger LOGGER = getLogger(NodeMutation.class);
    private final Probability individualMutationProbability;
    private final Probability nodeMutationProbability;
    private Random rng;

    /**
     *
     * @param individualMutationProbability probability of an individual strategy being mutated at all
     * @param nodeMutationProbability probability for each node in the "genome" being mutated.
     */
    public NodeMutation(Probability individualMutationProbability, Probability nodeMutationProbability) {
        this.individualMutationProbability = individualMutationProbability;
        this.nodeMutationProbability = nodeMutationProbability;
    }

    @Override
    public List<GeneticStrategy> apply(List<GeneticStrategy> selectedCandidates, Random rng) {
        this.rng = rng;
        List<GeneticStrategy> mutatedStrategies = selectedCandidates.stream()
                         .map(this::mutateStrategy)
                         .collect(toList());
        return mutatedStrategies;
    }

    private GeneticStrategy mutateStrategy(GeneticStrategy strategy) {
        if (individualMutationProbability.nextEvent(rng)) {
            GeneticStrategy mutant = new GeneticStrategy(strategy);
            Node<Range, BidEvaluationContext> mutantBidNode = mutant.getBidNode();
            mutantBidNode.accept(node -> {
                if (nodeMutationProbability.nextEvent(rng)) {
                    node.mutate(rng);
                }
            });
            Node<Range, BidEvaluationContext> bidNode = strategy.getBidNode();
            LOGGER.debug("Tried mutating bid evaluator #{} -> #{} {} -> {}",
                          toHexString(bidNode.hashCode()), toHexString(mutantBidNode.hashCode()), bidNode,
                          mutantBidNode);
            return mutant;
        }
        return strategy;
    }

}
