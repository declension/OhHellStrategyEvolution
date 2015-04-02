package net.declension.ea.cards.ohhell.nodes;

import java.util.Optional;

/**
 * Models, simply, the item under evaluation, e.g. bid or card.
 */
public class ItemNode<I extends Number,C> extends Node<I,C> {

    @Override
    public Node<I, C> shallowCopy() {
        return new ItemNode<>();
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        return item;
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }

    @Override
    public String toString() {
        return "x";
    }
}
