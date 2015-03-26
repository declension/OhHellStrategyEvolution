package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.BidEvaluationContext;

import java.util.Optional;

public class RemainingBidNode extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(BidEvaluationContext context) {
        return context.getRemainingBidCount();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }
}
