package net.declension.games.cards.ohhell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class BidBustingRulesBidValidator implements BidValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidBustingRulesBidValidator.class);
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
        return !roundFinished(bids) || totalFor(bids) != trickSize;
    }

    private boolean roundFinished(Collection<Integer> bids) {
        return bids.stream().noneMatch(v -> v == null);
    }

    private int totalFor(Collection<Integer> bids) {
        return bids.stream().mapToInt(Integer::intValue).sum();
    }

    private boolean validEntry(Integer bid) {
        return bid == null || (bid >= 0 && bid <= trickSize);
    }
}
