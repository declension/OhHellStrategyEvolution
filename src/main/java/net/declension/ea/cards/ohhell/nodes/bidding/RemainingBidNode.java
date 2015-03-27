package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;

import java.util.Optional;

public class RemainingBidNode extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(Range bid, BidEvaluationContext context) {
        return context.getRemainingBidCount();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }

    @Override
    public String toString() {
        return "RemainingBids";
    }
}
