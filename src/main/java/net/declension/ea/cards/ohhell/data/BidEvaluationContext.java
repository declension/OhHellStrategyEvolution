package net.declension.ea.cards.ohhell.data;

import java.util.List;

/**
 * The idea is to reduce complexity of the search space.
 */
public interface BidEvaluationContext extends InGameEvaluationContext {
    /*
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids ) {
    */

    /**
     * A list of the bids so far, in order (first is to the left of the dealer)
     */
    List<Range> getBidsSoFar();

    /**
     * This should be ranged within the known (reasonable) limits.
     * @return
     */
    Range getRemainingBidCount();

}
