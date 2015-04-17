package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static net.declension.utils.StringUtils.tidyNumber;

public class ConstantNode<I, C> extends TerminalNode<I, C> {
    private Number value;

    public ConstantNode(Number value) {
        requireNonNull(value, "Constant value");
        this.value = value;
    }

    public static <I, C> Node<I, C> constant(Number value) {
        return new ConstantNode<>(value);
    }

    public static <I, C> Node<I, C> deadNumber() {
        return new ConstantNode<>(Double.NaN);
    }

    public Number getValue() {
        return value;
    }

    @Override
    public Number doEvaluation(I item, C context) {
        return value;
    }

    @Override
    public Node<I,C> mutate(Random rng) {
        // Allow +/- 100% for now
        double newValue = value.doubleValue() * (rng.nextDouble() * 1.5 + 0.5);

        // Don't ternary-ify this, the autoboxing goes mental.
        if (value instanceof Integer) {
            value = (int) Math.round(newValue);
        } else {
            value = newValue;
        }
        return this;
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new ConstantNode<>(value);
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

        ConstantNode constantNode = (ConstantNode) other;
        return value.equals(constantNode.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
