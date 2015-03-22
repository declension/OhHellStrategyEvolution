package net.declension.games.cards.ohhell;

import net.declension.collections.CollectionUtils;

import java.util.Collection;
import java.util.Optional;

import static net.declension.collections.CollectionUtils.allPresent;

public class BidBustingRulesBidValidator implements BidValidator {
    private Integer trickSize;

    public BidBustingRulesBidValidator(Integer trickSize) {
        this.trickSize = trickSize;
    }

    @Override
    public boolean test(AllBids bids) {
        return noInvalidBids(bids.values()) && completeRoundsObeyBidBusting(bids.values());
    }

    private boolean noInvalidBids(Collection<Optional<Integer>> bids) {
        return bids.stream().allMatch(this::validEntry);
    }

    private boolean completeRoundsObeyBidBusting(Collection<Optional<Integer>> bids) {
        return !roundFinished(bids) || CollectionUtils.totalOfOptionals(bids) != trickSize;
    }

    private boolean roundFinished(Collection<Optional<Integer>> bids) {
        return allPresent(bids);
    }

    private boolean validEntry(Optional<Integer> bid) {
        return !bid.isPresent() || (bid.get() >= 0 && bid.get() <= trickSize);
    }
}
