package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.ListNumber;

import static java.lang.String.format;
import static net.declension.ea.cards.ohhell.data.SingleItemListNumber.singleItemOf;

public class ConstNode<T> extends TerminalNode<T> {
    private final ListNumber value;

    public ConstNode(Number value) {
        this.value = singleItemOf(value);
    }

    public static <T> Node<T> constant(Number value) {
        return new ConstNode<>(value);
    }

    @Override
    public ListNumber evaluate(T context) {
        return value;
    }

    @Override
    public String toString() {
        return format("%s", value);
    }
}
