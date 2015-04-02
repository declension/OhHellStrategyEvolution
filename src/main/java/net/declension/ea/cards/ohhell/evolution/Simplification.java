package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class Simplification implements EvolutionaryOperator<GeneticStrategy> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simplification.class);
    private final Probability individualModificationProbability;
    private Random rng;

    /**
     * @param individualModificationProbability probability of an individual strategy being simplified
     */
    public Simplification(Probability individualModificationProbability) {

        this.individualModificationProbability = individualModificationProbability;
    }


    @Override
    public List<GeneticStrategy> apply(List<GeneticStrategy> selectedCandidates, Random rng) {
        this.rng = rng;
        return selectedCandidates.stream()
                                 .map(this::simplifiedStrategy)
                                 .collect(toList());
    }

    private GeneticStrategy simplifiedStrategy(GeneticStrategy strategy) {
        if (individualModificationProbability.nextEvent(rng)) {
            GeneticStrategy ret = new GeneticStrategy(strategy);
            //int numNodes = bidEvaluator.allDescendants().size();
            Node<Range, BidEvaluationContext> bidEvaluator = strategy.getBidEvaluator();
            LOGGER.debug("Before: {}", bidEvaluator);
            Node<Range, BidEvaluationContext> simplifiedVersion = bidEvaluator.simplifiedVersion();
            if (bidEvaluator.equals(simplifiedVersion)) {
                // No point creating identical copies...
                return strategy;
            }
            ret.setBidEvaluator(simplifiedVersion);
            LOGGER.debug(" After: {}", simplifiedVersion);
            return ret;
        }
        return strategy;
    }
}
