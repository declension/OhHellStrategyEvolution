package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.BiddingDecisionData;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

public class RemainingBidNode extends Node<BiddingDecisionData> {

    @Override
    public Number evaluate(BiddingDecisionData context) {
        return context.getRemainingBidCount();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }
}
