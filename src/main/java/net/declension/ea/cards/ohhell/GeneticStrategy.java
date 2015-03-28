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
import net.declension.games.cards.ohhell.strategy.playing.RandomPlayingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.slightlyRandomisedDoubleValueSorting;

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
public class GeneticStrategy implements OhHellStrategy, RandomPlayingStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticStrategy.class);
    private final GameSetup gameSetup;

    Node<Range, BidEvaluationContext> rootBiddingNode;

    public GeneticStrategy(GameSetup gameSetup, Node<Range, BidEvaluationContext> rootBiddingNode) {
        this.gameSetup = gameSetup;
        this.rootBiddingNode = rootBiddingNode;
    }

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        BidEvaluationContext context
                = new BiddingStrategyToBidEvaluationContextAdapter(gameSetup, trumps, me, myCards, bidsSoFar,
                                                                   allowedBids);
        LOGGER.debug("For potential bids {}, evaluating: {}", allowedBids, rootBiddingNode);
        Map<Integer, Number> weights = allowedBids.stream()
               .collect(toMap(Function.<Integer>identity(), bid -> resultForProposedBid(bid, context, myCards)));

        Integer ret = weights.entrySet().stream()
                             .max(slightlyRandomisedDoubleValueSorting())
                             .get().getKey();
        return ret;
    }

    private Number resultForProposedBid(Integer bid, BidEvaluationContext context, Set<Card> myCards) {
        return rootBiddingNode.evaluate(new Range(bid, 0, myCards.size()), context).doubleValue();
    }

    @Override
    public String toString() {
        return format("<GEN#%s|GEN>", toHexString(rootBiddingNode.hashCode()));
    }

    @Override
    public String fullDetails() {
        return format("%s: bid -> %s", toString(), rootBiddingNode);
    }

    @Override
    public Random getRng() {
        return gameSetup.getRNG();
    }
}
