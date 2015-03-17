package net.declension.games.cards.ohhell;

import java.util.Collection;

import static net.declension.collections.CollectionUtils.containsNoNulls;
import static net.declension.collections.CollectionUtils.totalOf;

public class BidBustingRulesBidValidator implements BidValidator {
    private Integer trickSize;

    public BidBustingRulesBidValidator(Integer trickSize) {
        this.trickSize = trickSize;
    }

    @Override
    public boolean test(AllBids bids) {
        return noInvalidBids(bids.values()) && completeRoundsObeyBidBusting(bids.values());
    }

    private boolean noInvalidBids(Collection<Integer> bids) {
        return bids.stream().allMatch(this::validEntry);
    }

    private boolean completeRoundsObeyBidBusting(Collection<Integer> bids) {
        return !roundFinished(bids) || totalOf(bids) != trickSize;
    }

    private boolean roundFinished(Collection<Integer> bids) {
        return containsNoNulls(bids);
    }

    private boolean validEntry(Integer bid) {
        return bid == null || (bid >= 0 && bid <= trickSize);
    }
}
