package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.BiddingDecisionData;
import net.declension.ea.cards.ohhell.data.ListNumber;

import java.util.Optional;

import static net.declension.ea.cards.ohhell.data.SingleItemListNumber.singleItemOf;

public class BiddingDataNode extends Node<BiddingDecisionData> {


    @Override
    public ListNumber evaluate(BiddingDecisionData context) {
        return singleItemOf(context.getRemainingBidCount());
    }

    @Override
    public Optional<Integer> arity() {
        return null;
    }
}
