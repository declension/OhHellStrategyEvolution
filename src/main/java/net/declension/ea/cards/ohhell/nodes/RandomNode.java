package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

public class RandomNode<T> extends TerminalNode<T> {
    private final Random rng;

    public RandomNode(Random rng) {
        this.rng = rng;
    }

    @Override
    protected Number doEvaluation(T context) {
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
}
