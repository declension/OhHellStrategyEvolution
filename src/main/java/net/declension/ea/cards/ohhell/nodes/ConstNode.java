package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static net.declension.utils.StringUtils.tidyNumber;

public class ConstNode<I, C> extends TerminalNode<I, C> {
    private final Number value;

    public ConstNode(Number value) {
        requireNonNull(value, "Constant value");
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
    public <T extends Node<I, C>> T mutatedCopy(Random rng) {
        // Allow +/- 40% for now
        double newValue = value.doubleValue() * (rng.nextDouble() * 0.8 + 0.6);

        if (value instanceof Integer) {
            return (T) new ConstNode<I, C>((int) Math.round(newValue));
        };
        return (T) new ConstNode<I, C>(newValue);
    }

    @Override
    public String toString() {
        return tidyNumber(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        ConstNode constNode = (ConstNode) other;
        return value.equals(constNode.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
