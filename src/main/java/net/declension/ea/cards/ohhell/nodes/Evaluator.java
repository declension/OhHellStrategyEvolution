package net.declension.ea.cards.ohhell.nodes;

public interface Evaluator<I,C> {

    Number evaluate(I item, C context);
}
