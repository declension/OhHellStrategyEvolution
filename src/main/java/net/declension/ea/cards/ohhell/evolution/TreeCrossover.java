package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class TreeCrossover extends AbstractCrossover<GeneticStrategy> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeCrossover.class);
    /**
     * Any less than this => don't bother with mating as it doesn't make sense.
     */
    public static final int MINIMUM_NODE_COUNT = 3;

    /**
     * Creates a single-point cross-over operator.
     * @param crossoverProbability
     */
    public TreeCrossover(Probability crossoverProbability) {
        super(1, crossoverProbability);
    }

    /**
     * Swaps randomly selected sub-trees between the two parents.
     * @param mum The first parent.
     * @param dad The second parent.
     * @param numberOfCrossoverPoints The number of cross-overs to perform.
     * @param rng A source of randomness.
     * @return A list of two offspring, generated by swapping sub-trees
     * between the two parents.
     */
    @Override
    protected List<GeneticStrategy> mate(GeneticStrategy mum,
                                         GeneticStrategy dad,
                                         int numberOfCrossoverPoints,
                                         Random rng) {
        GeneticStrategy daughter = new GeneticStrategy(mum);
        GeneticStrategy son = new GeneticStrategy(dad);
        IntStream.rangeClosed(1, numberOfCrossoverPoints).forEach(i -> {
            Node<Range, BidEvaluationContext> mumNode = mum.getBidEvaluator();
            Node<Range, BidEvaluationContext> dadNode = dad.getBidEvaluator();
            int mumNodeCount = mumNode.countNodes();
            int dadNodeCount = dadNode.countNodes();
            if (mumNodeCount >= MINIMUM_NODE_COUNT && dadNodeCount >= MINIMUM_NODE_COUNT) {
                LOGGER.info("Mating bid nodes: {} and {}...", mumNode, dadNode);
                int mumCrossoverPoint = rng.nextInt(mumNodeCount - MINIMUM_NODE_COUNT) + 1;
                int dadCrossoverPoint = rng.nextInt(dadNodeCount - MINIMUM_NODE_COUNT) + 1;
                Node<Range, BidEvaluationContext> replacedMum
                        = mumNode.copyWithReplacedNode(mumCrossoverPoint, dadNode.getNode(dadCrossoverPoint));
                Node<Range, BidEvaluationContext> replacedDad
                        = dadNode.copyWithReplacedNode(dadCrossoverPoint, mumNode.getNode(mumCrossoverPoint));
                daughter.setBidEvaluator(replacedMum);
                son.setBidEvaluator(replacedDad);
                LOGGER.info("To produce these: {} and {}...", replacedMum, replacedDad);
            } else {
                LOGGER.debug("Not mating with counts of {} and {}", mumNodeCount, dadNodeCount);
            }
        });
        return asList(daughter, son);
    }
}