package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.TerminalNode;

public class HandSize<I, C extends InGameEvaluationContext> extends TerminalNode<I, C> {

    public static <I, C extends InGameEvaluationContext> Node<I, C> handSize() {
        return new HandSize<>();
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        return context.handSize();
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new HandSize<>();
    }

    @Override
    public String toString() {
        return "handSize";
    }
}
