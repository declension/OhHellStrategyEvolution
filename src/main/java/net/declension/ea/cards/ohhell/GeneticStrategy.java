package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import net.declension.games.cards.ohhell.strategy.playing.SimplePlayingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.slightlyRandomisedDoubleValueSorting;
import static net.declension.utils.Validation.requireNonNullParam;

/**
 * The million-dollar class.
 * Decides how to bid and choose cards based on the current object genome.
 *  <ol>
 *    <li>Reduce my cards to virtual suits: trumps and three sets of non-trumps</li>
 *    <li>Evaluate the genetic symbol tree representing card fitness function</li>
 *    <li>Iterate over allowed bids, apply weighting function</li>
 *    <li>Choose with a genome-based fuzz factor</li>
 *    <li>Profit</li>
 *  </ol>
 */
public class GeneticStrategy implements OhHellStrategy, SimplePlayingStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticStrategy.class);
    private final GameSetup gameSetup;

    Node<Range, BidEvaluationContext> bidEvaluator;

    public GeneticStrategy(GameSetup gameSetup, Node<Range, BidEvaluationContext> bidEvaluator) {
        requireNonNullParam(gameSetup, "Game Setup");
        requireNonNullParam(bidEvaluator, "Bid Evaluator");
        this.gameSetup = gameSetup;
        this.bidEvaluator = bidEvaluator;
    }

    public GeneticStrategy(GeneticStrategy strategy) {
        this(strategy.gameSetup, strategy.bidEvaluator.deepCopy());
    }

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        BidEvaluationContext context
                = new BiddingStrategyToBidEvaluationContextAdapter(gameSetup, trumps, myCards, bidsSoFar);
        LOGGER.debug("For potential bids {}, evaluating: {}", allowedBids, bidEvaluator);
        Map<Integer, Number> weights = allowedBids.stream()
               .collect(toMap(Function.<Integer>identity(), bid -> resultForProposedBid(bid, context, myCards)));

        return weights.entrySet().stream()
                                 .max(slightlyRandomisedDoubleValueSorting())
                                 .get().getKey();
    }

    private Number resultForProposedBid(Integer bid, BidEvaluationContext context, Set<Card> myCards) {
        return bidEvaluator.evaluate(new Range(bid, 0, myCards.size()), context).doubleValue();
    }

    @Override
    public String toString() {
        return format("GEN#%s|SIM", toHexString(bidEvaluator.hashCode()));
    }

    @Override
    public String fullDetails() {
        return format("%s: x -> %s", toString(), bidEvaluator);
    }

    @Override
    public GameSetup getGameSetup() {
        return gameSetup;
    }

    public Node<Range, BidEvaluationContext> getBidEvaluator() {
        return bidEvaluator;
    }

    public void setBidEvaluator(Node<Range, BidEvaluationContext> bidEvaluator) {
        this.bidEvaluator = bidEvaluator;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        GeneticStrategy that = (GeneticStrategy) other;
        return bidEvaluator.equals(that.bidEvaluator) && gameSetup.equals(that.gameSetup);
    }

    @Override
    public int hashCode() {
        int result = gameSetup.hashCode();
        result = 31 * result + bidEvaluator.hashCode();
        return result;
    }
}
