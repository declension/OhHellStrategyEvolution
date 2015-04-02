package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.Aggregator;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.ea.cards.ohhell.data.Aggregator.ALL_AGGREGATORS;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;


public class AggregatingNode<I,C> extends Node<I,C> {

    private Aggregator aggregator;

    public AggregatingNode(Aggregator aggregator) {
        requireNonNull(aggregator);
        this.aggregator = aggregator;
    }

    public AggregatingNode(Aggregator aggregator, Node<I, C>...children) {
        this(aggregator);
        this.children = asList(children);
    }

    public static <I,T> AggregatingNode<I,T> aggregator(Aggregator aggregator) {
        return new AggregatingNode<>(aggregator);
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
    public Node<I, C> simplifiedVersion() {
        if (aggregator == Aggregator.COUNT) {
            // Perhaps that aggregator *is* pointless, but, well, it's got plenty of believable uses.
            return constant(children.size());
        }
        switch (children.size()) {
            case 0:
                return constant(aggregator.identityValue());
            case 1:
                switch (aggregator) {
                    case VARIANCE: return constant(0);
                    default: return child(0);
                }
            default:
                List<Node<I, C>> simpleChildren = children.stream()
                                                          .map(Node::simplifiedVersion)
                                                          .collect(toList());
                if (simpleChildren.stream().allMatch(n -> n instanceof ConstantNode)) {
                    return constant(aggregator.apply(simpleChildren.stream()
                                                                   .map(n -> ((ConstantNode) n).getValue())
                                                                   .collect(toList()),
                                                     getComparator()));
                }
                Node<I, C> simple = shallowCopy();
                simple.setChildren(simpleChildren);
                return simple;
        }
    }

    @Override
    public Number doEvaluation(I item, C context) {
        List<Number> values = children().stream()
                                        .map(n -> n.evaluate(item, context))
                                        .collect(toList());
        return aggregator.apply(values, getComparator());
    }

    @Override
    public Node<I, C> shallowCopy() {
        return new AggregatingNode<>(aggregator);
    }


    private Comparator<Number> getComparator() {
        return Comparator.comparing(Number::doubleValue);
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return format("%s(%s)", aggregator, children);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }

        AggregatingNode that = (AggregatingNode) other;
        return aggregator == that.aggregator;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregator.hashCode();
        return result;
    }
}
