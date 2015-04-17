package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.Aggregator;
import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.AggregatedNode;
import net.declension.ea.cards.ohhell.nodes.ConstantNode;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;
import static net.declension.utils.Validation.numberWithinRange;

public class AggregatedRankData extends AggregatedNode<Range, InGameEvaluationContext> {

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
    public Node<Range, InGameEvaluationContext> simplifiedVersion() {
        Node<Range, InGameEvaluationContext> ret = shallowCopy();
        Node<Range, InGameEvaluationContext> child = child(0).simplifiedVersion();
        if (child instanceof ConstantNode && !CollectionType.validIndex(((ConstantNode) child).getValue().intValue())) {
            return deadNumber();
        }
        ret.addChild(child);
        return ret;
    }

    public static AggregatedRankData aggregatedRankData(Aggregator aggregator) {
        return new AggregatedRankData(aggregator);
    }

    @Override
    protected Number doEvaluation(Range item, InGameEvaluationContext context) {
        Optional<CollectionType> collectionType = getCollectionType(item, context);
        return collectionType.map(type -> aggregator.apply(type.data(context)))
                             .orElse(Double.NaN);

    }

    private Optional<CollectionType> getCollectionType(Range item, InGameEvaluationContext context) {
        return CollectionType.fromNumber(child(0).evaluate(item, context));
    }

    @Override
    public Node<Range, InGameEvaluationContext> shallowCopy() {
        return new AggregatedRankData(aggregator);
    }

    @Override
    public String toString() {
        return format("%s(rankData<%s>)", aggregator, friendlyChildDescription());
    }

    private String friendlyChildDescription() {
        Node<Range, InGameEvaluationContext> node = child(0);
        if (node instanceof ConstantNode) {
            Integer value = ((ConstantNode) node).getValue().intValue();
            return CollectionType.fromNumber(value).map(Enum::toString).orElse("INVALID");
        }
        return node.toString();
    }
}
