package net.declension.ea.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The million-dollar class.
 * Decides how to bid and choose cards based on the current object genome.
 *  <ol>
 *    <li>Reduce my cards to virtual suits: trumps and three sets of non-trumps</li>
 *    <li>Provide count, min, max, mean, VARIANCE of each set as double primitives
 *    using {@link net.declension.ea.cards.ohhell.BiddingDecisionData}</li>
 *    <li>Produce symbol tree representing card fitness function</li>
 *    <li>Iterate over allowed bids, apply weighting function</li>
 *    <li>Choose with a genome-based fuzz factor</li>
 *    <li>Profit</li>
 *  </ol>
 */
public class GeneticOhHellStrategy implements OhHellStrategy {

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids ) {
        return null;

    }

    @Override
    public Card chooseCard(Optional<Suit> trumps, Player me, Set<Card> myCards, Map<Player, Integer> tricksBid,
                           Map<Player, Integer> tricksTaken, Trick trickSoFar, Set<Card> allowedCards) {
        return null;
    }
}
