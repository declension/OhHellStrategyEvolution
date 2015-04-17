package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.Aggregator;

import java.util.Optional;
import java.util.Random;

import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.ea.cards.ohhell.data.Aggregator.ALL_AGGREGATORS;

public abstract class AggregatedNode<I, C> extends Node<I,C> {
    protected Aggregator aggregator;

    public AggregatedNode(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public Node<I,C> mutate(Random rng) {
        Aggregator newAgg;
        do {
            newAgg = pickRandomEnum(rng, Aggregator.class);
        } while (ALL_AGGREGATORS.size() > 1 && newAgg == aggregator);
        logger.debug("Mutating {}: {} -> {}", this, aggregator, newAgg);
        aggregator = newAgg;
        return this;
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(1);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        AggregatedNode<?, ?> that = (AggregatedNode<?, ?>) o;
        return aggregator == that.aggregator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (aggregator != null ? aggregator.hashCode() : 0);
        return result;
    }
}
