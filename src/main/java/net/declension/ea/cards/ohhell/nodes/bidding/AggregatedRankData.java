package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.Aggregator;
import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.nodes.AggregatedNode;
import net.declension.ea.cards.ohhell.nodes.ConstantNode;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static java.lang.String.format;
import static net.declension.collections.CollectionUtils.pickRandomEnum;
import static net.declension.ea.cards.ohhell.data.Aggregator.ALL_AGGREGATORS;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;
import static net.declension.utils.Validation.numberWithinRange;

public class AggregatedRankData<I, C extends InGameEvaluationContext> extends AggregatedNode<I, C> {

    public enum CollectionType {
        TRUMPS(InGameEvaluationContext::myTrumpsCardRanks),
        LEADS(ctx -> ctx.myOtherSuitsCardRanks().get(0)),
        THIRD_SUIT(ctx -> ctx.myOtherSuitsCardRanks().get(1)),
        FOURTH_SUIT(ctx -> ctx.myOtherSuitsCardRanks().get(2)),
        ;
        private final Function<InGameEvaluationContext, List<? extends Number>> getCollectionFunction;

        CollectionType(Function<InGameEvaluationContext, List<? extends Number>> getCollection) {
            getCollectionFunction = getCollection;
        }

        public List<Number> data(InGameEvaluationContext context) {
            return (List<Number>) getCollectionFunction.apply(context);
        }

        /**
         * Uses the to return an appropriate enum, if possible.
         * @param number the number to convert
         * @return an Optional enum.
         */
        public static Optional<CollectionType> fromNumber(Number number) {
            CollectionType[] values = values();
            int index = number.intValue();
            if (validIndex(index)) {
                return Optional.of(values[index]);
            }
            return Optional.empty();
        }

        public static boolean validIndex(int index) {
            return numberWithinRange(index, 0, values().length - 1);
        }
    }

    public AggregatedRankData(Aggregator aggregator) {
        super(aggregator);
    }

    @Override
    public Node<I, C> simplifiedVersion() {
        Node<I, C> ret = shallowCopy();
        Node<I, C> child = child(0).simplifiedVersion();
        if (child instanceof ConstantNode) {
            Number value = ((ConstantNode) child).getValue();
            if (CollectionType.validIndex(value.intValue())) {
                child = constant(value.intValue());
            } else {
                return deadNumber();
            }
        }
        ret.addChild(child);
        return ret;
    }

    public static <I,C extends InGameEvaluationContext> Node<I, C> aggregatedRankData(Aggregator aggregator,
                                                        Node<I, C> child) {
        Node<I,C> node = new AggregatedRankData<>(aggregator);
        node.addChild(child);
        return node;
    }

    public static <I, C extends InGameEvaluationContext> Node<I,C> aggregatedTrumpsData(Aggregator aggregator) {
        return aggregatedRankData(aggregator, constant(CollectionType.TRUMPS.ordinal()));
    }

    @Override
    protected Number doEvaluation(I item, C context) {
        Optional<CollectionType> collectionType = getCollectionType(item, context);
        return collectionType.map(type -> aggregator.apply(type.data(context)))
                             .orElse(Double.NaN);
    }

    @Override
    public Node<I, C> mutate(Random rng) {
        Aggregator newAgg;
        do {
            newAgg = pickRandomEnum(rng, Aggregator.class);
        } while (ALL_AGGREGATORS.size() > 1 && newAgg == aggregator);
        logger.debug("Mutating {}: {} -> {}", this, aggregator, newAgg);
        aggregator = newAgg;
        return this;
    }

    private Optional<CollectionType> getCollectionType(I item, C context) {
        return CollectionType.fromNumber(child(0).evaluate(item, context));
    }

    @Override
    protected Node<I, C> shallowCopy() {
        return new AggregatedRankData<>(aggregator);
    }

    @Override
    public String toString() {
        return format("%s(rankData<%s>)", aggregator, friendlyChildDescription());
    }

    private String friendlyChildDescription() {
        Node<I, C> node = child(0);
        if (node instanceof ConstantNode) {
            Integer value = ((ConstantNode) node).getValue().intValue();
            return CollectionType.fromNumber(value).map(Enum::toString).orElse("INVALID");
        }
        return node.toString();
    }
}
