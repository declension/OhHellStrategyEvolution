package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;

public class NumPlayers<I, C extends InGameEvaluationContext> extends TerminalNode<I, C> {

    public static <I, C extends InGameEvaluationContext> Node<I, C> numPlayers() {
        return new NumPlayers<>();
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        return context.numPlayers();
    }

    @Override
    protected Node<I, C> shallowCopy() {
        return new NumPlayers<>();
    }

    @Override
    public String toString() {
        return "numPlayers";
    }
}
