package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

public class BidNode extends BaseBiddingMethodNode {

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new BidNode();
    }

    @Override
    protected Number doEvaluation(Range bid, BidEvaluationContext context) {
        return bid;
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }

    @Override
    public String toString() {
        return "bid";
    }
}
