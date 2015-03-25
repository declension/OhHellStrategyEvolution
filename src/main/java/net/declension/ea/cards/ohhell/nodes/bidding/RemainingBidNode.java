package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.BiddingDecisionData;
import net.declension.ea.cards.ohhell.data.ListNumber;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

import static net.declension.ea.cards.ohhell.data.SingleItemListNumber.singleItemOf;

public class RemainingBidNode extends Node<BiddingDecisionData> {

    @Override
    public ListNumber evaluate(BiddingDecisionData context) {
        return singleItemOf(context.getRemainingBidCount());
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }
}
