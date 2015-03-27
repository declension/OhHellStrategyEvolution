package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

public class RandomNode<I, C> extends TerminalNode<I, C> {
    private final Random rng;

    public RandomNode(Random rng) {
        this.rng = rng;
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

            @Override
            public String toString() {
                return "RND()";
            }
        };
    }


}
