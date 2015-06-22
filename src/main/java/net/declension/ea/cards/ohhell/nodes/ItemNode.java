package net.declension.ea.cards.ohhell.nodes;

/**
 * Models, simply, the item under evaluation, e.g. bid or card.
 */
public class ItemNode<I extends Number,C> extends TerminalNode<I,C> {

    public static <I extends Number, C> Node<I, C> item() {
        return new ItemNode<>();
    }

    @Override
    protected Node<I, C> shallowCopy() {
        return new ItemNode<>();
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        return item;
    }

    @Override
    public String toString() {
        return "x";
    }
}
