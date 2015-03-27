package net.declension.ea.cards.ohhell.nodes;

import static java.lang.String.format;

public class ConstNode<I, C> extends TerminalNode<I, C> {
    private final Number value;

    public ConstNode(Number value) {
        this.value = value;
    }

    public static <I, C> Node<I, C> constant(Number value) {
        return new ConstNode<>(value);
    }

    @Override
    public Number doEvaluation(I item, C context) {
        return value;
    }

    @Override
    public String toString() {
        return format("%s", value);
    }
}
