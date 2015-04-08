package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.Aggregator;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.AggregatedNode;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static net.declension.utils.Validation.numberWithinRange;

public class AggregatedBiddingData extends AggregatedNode<Range, BidEvaluationContext> {

    public enum CollectionType {
        TRUMPS(InGameEvaluationContext::myTrumpsCardRanks),
        LEADS(ctx -> ctx.myOtherSuitsCardRanks().get(0)),
        THIRD_SUIT(ctx -> ctx.myOtherSuitsCardRanks().get(1)),
        FOURTH_SUIT(ctx -> ctx.myOtherSuitsCardRanks().get(2)),
        ;
        private final Function<BidEvaluationContext, List<? extends Number>> getCollectionFunction;

        CollectionType(Function<BidEvaluationContext, List<? extends Number>> getCollection) {
            getCollectionFunction = getCollection;
        }

        public List<Number> data(BidEvaluationContext context) {
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
            if (numberWithinRange(index, 0, values.length - 1)) {
                return Optional.of(values[index]);
            }
            return Optional.empty();
        }
    }

    public AggregatedBiddingData(Aggregator aggregator) {
        super(aggregator);
    }

    public static AggregatedBiddingData aggregatedBiddingData(Aggregator aggregator) {
        return new AggregatedBiddingData(aggregator);
    }

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        Optional<CollectionType> collectionType = getCollectionType(item, context);
        return collectionType.map(type -> aggregator.apply(type.data(context)))
                             .orElse(Double.NaN);

    }

    private Optional<CollectionType> getCollectionType(Range item, BidEvaluationContext context) {
        return CollectionType.fromNumber(child(0).evaluate(item, context));
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new AggregatedBiddingData(aggregator);
    }

    @Override
    public String toString() {
        return format("%s(bidData[%s])", aggregator, child(0));
    }
}