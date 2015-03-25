package net.declension.ea.cards.ohhell;

import java.util.List;

/**
 * The idea is to reduce complexity of the search space.
 */
public interface BiddingDecisionData {
    /*
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards, AllBids bidsSoFar,
                             Set<Integer> allowedBids ) {
    */

    /**
     *
     * @return
     */
    StatsList<RankRanking> getTrumpsRanks();

    /**
     * The ranks of each of the non-trumps suits.
     * @return a 3 or 4 item list
     */
    List<StatsList<RankRanking>> getOtherRanks();

    StatsList<Range> getBidsSoFar();

    Range getRemainingBidCount();

}
