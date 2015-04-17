package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.TerminalNode;

import java.util.Optional;

public class RemainingBidNode extends TerminalNode<Range, BidEvaluationContext> {

    @Override
    protected Number doEvaluation(Range bid, BidEvaluationContext context) {
        return context.getRemainingBidCount();
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new RemainingBidNode();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }

    @Override
    public String toString() {
        return "remainingBids";
    }
  }
