package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.TerminalNode;

public class BidsSoFar extends TerminalNode<Range, BidEvaluationContext> {

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        return context.getBidsSoFar().stream()
                                     .mapToInt(Range::intValue)
                                     .sum();
    }

    @Override
    protected Node<Range, BidEvaluationContext> shallowCopy() {
        return new BidsSoFar();
    }

    @Override
    public String toString() {
        return "bidsSoFar";
    }
}
