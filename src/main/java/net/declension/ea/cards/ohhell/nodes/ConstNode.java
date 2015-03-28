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
        return format(value instanceof Double? "%.3f" : "%s", value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstNode)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ConstNode constNode = (ConstNode) o;
        return !(value != null ? !value.equals(constNode.value) : constNode.value != null);
    }

    @Override
    public int hashCode() {
        return 31 * getClass().getSimpleName().hashCode() + (value != null ? value.hashCode() : 0);
    }
}
