package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.TerminalNode;

public class HandSize<I> extends TerminalNode<I, InGameEvaluationContext> {

    @Override
    protected Number doEvaluation(I item, InGameEvaluationContext context) {
        return context.handSize();
    }

    @Override
    public Node<I, InGameEvaluationContext> shallowCopy() {
        return new HandSize<>();
    }

    @Override
    public String toString() {
        return "handSize";
    }
}
