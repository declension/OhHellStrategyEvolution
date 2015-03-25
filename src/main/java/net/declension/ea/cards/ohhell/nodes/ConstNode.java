package net.declension.ea.cards.ohhell.nodes;

import static java.lang.String.format;

public class ConstNode<T> extends TerminalNode<T> {
    private final Number value;

    public ConstNode(Number value) {
        this.value = value;
    }

    public static <T> Node<T> constant(Number value) {
        return new ConstNode<>(value);
    }

    @Override
    public Number evaluate(T context) {
        return value;
    }

    @Override
    public String toString() {
        return format("%s", value);
    }
}
