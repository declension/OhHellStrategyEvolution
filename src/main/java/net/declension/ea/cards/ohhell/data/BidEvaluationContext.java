package net.declension.ea.cards.ohhell.data;

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

}
