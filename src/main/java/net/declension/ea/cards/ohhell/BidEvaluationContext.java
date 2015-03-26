package net.declension.ea.cards.ohhell;

import java.util.List;

/**
 * The idea is to reduce complexity of the search space.
 */
public interface BidEvaluationContext {
    /*
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids ) {
    */


    /**
     * The proposed bid to evaluate
     *
     * @return
     */
    Range proposedBid();

    /**
     * The rankings of the trumps.
     * @return a no-duplicate list of rankings in the range (1-13)
     */
    List<RankRanking> getTrumpsRanks();

    /**
     * The ranks of each of the non-trumps suits.
     * @return a 3 or 4 item list
     */
    List<List<RankRanking>> getOtherRanks();

    /**
     * A list of the bids so far, in order (first is to the left of the dealer)
     */
    List<Range> getBidsSoFar();

    /**
     * This should be ranged within the known (reasonable) limits.
     * @return
     */
    Range getRemainingBidCount();

    /**
     * This should set the current bid, and return an instance
     * to allow fluent usage.
     * @param bid The bid to evaluate
     * @return the current context instance
     */
    BidEvaluationContext withProposedBid(Integer bid);
}
