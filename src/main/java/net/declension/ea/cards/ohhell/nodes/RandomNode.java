package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;

public class RandomNode<I, C> extends TerminalNode<I, C> {
    private final Random rng;

    public RandomNode(Random rng) {
        this.rng = rng;
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new RandomNode<>(rng);
    }

    @Override
    public Node<I, C> mutate(Random rng) {
        // Some pressure to de-random is probably good here anyway.
        return constant(rng.nextDouble());
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        return new Number() {
            @Override
            public int intValue() {
                return rng.nextInt();
            }

            @Override
            public long longValue() {
                return rng.nextLong();
            }

            @Override
            public float floatValue() {
                return rng.nextFloat();
            }

            @Override
            public double doubleValue() {
                return rng.nextDouble();
            }
        };
    }

    public String toString() {
        return "RND()";
    }
}
