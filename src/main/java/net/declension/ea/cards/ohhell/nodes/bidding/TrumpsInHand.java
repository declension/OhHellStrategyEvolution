package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.BidEvaluationContext;

import java.util.Optional;

/**
 * {@code TrumpsInHand(x,y)}
 * tries to get the {@code x}th rank (234...QKA) of a trump in hand,
 * or the {@code y} node if this errors.
 */
public class TrumpsInHand extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(BidEvaluationContext context) {
        try {
            return context.getTrumpsRanks().get(children.get(0).evaluate(context).intValue());
        } catch (IndexOutOfBoundsException e) {
            return children.get(1).evaluate(context);
        }
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }
}
