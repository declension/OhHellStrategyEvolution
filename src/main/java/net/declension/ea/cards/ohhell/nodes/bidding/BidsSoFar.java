package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

public class BidsSoFar extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        return context.getBidsSoFar().stream()
                                     .mapToInt(Range::intValue)
                                     .sum();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new BidsSoFar();
    }

    @Override
    public String toString() {
        return "bidsSoFar";
    }
}
