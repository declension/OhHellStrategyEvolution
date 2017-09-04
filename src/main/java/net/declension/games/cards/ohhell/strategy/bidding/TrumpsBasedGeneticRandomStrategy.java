package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.BinaryNode;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.declension.ea.cards.ohhell.data.Aggregator.COUNT;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.*;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.binary;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ItemNode.item;
import static net.declension.ea.cards.ohhell.nodes.bidding.AggregatedRankData.aggregatedTrumpsData;
import static net.declension.ea.cards.ohhell.nodes.bidding.HandSize.handSize;
import static net.declension.ea.cards.ohhell.nodes.NumPlayers.numPlayers;

/**
 * I think this roughly equates to:
 * {@code x -> 0 - ((((COUNT(rankData<TRUMPS>) - handSize * 12.0 / 51.0) / handSize + 1.0)
 *                  * handSize / numPlayers) - x) ^ 2}, but who really knows...
 */
public class TrumpsBasedGeneticRandomStrategy extends GeneticStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrumpsBasedGeneticRandomStrategy.class);
    static final Node<Range, BidEvaluationContext> BID_EVALUATOR = createBidEvaluator();
    static {
        LOGGER.warn("Hand-crafted trumps-based bid node={}", BID_EVALUATOR);
    }

    public TrumpsBasedGeneticRandomStrategy(GameSetup gameSetup) {
        super(gameSetup, BID_EVALUATOR);
    }

    private static Node<Range, BidEvaluationContext> createBidEvaluator() {
        // ((((COUNT(rankData<TRUMPS>) / handsize() - 12.0 / 51.0) + 1.0) * handSize / numPlayers) - x) ^ 2
        Node<Range, BidEvaluationContext> trumpsRatio = binary(DIVIDE, aggregatedTrumpsData(COUNT), handSize());
        Node<Range, BidEvaluationContext> trumpsDelta = binary(ADD, trumpsRatio, constant(1.0 - 12.0 / 51.0));
        BinaryNode<Range, BidEvaluationContext> expectedTrumps = binary(MULTIPLY, trumpsDelta, handSize());
        BinaryNode<Range, BidEvaluationContext> expected = binary(DIVIDE, expectedTrumps, numPlayers());
        Node<Range, BidEvaluationContext> diffNode = binary(SUBTRACT, expected, item());
        return binary(SUBTRACT, constant(0), binary(EXPONENTIATE, diffNode, constant(2)));
    }

    public String toString() {
        return "GEN#HAND|RND";
    }
}
