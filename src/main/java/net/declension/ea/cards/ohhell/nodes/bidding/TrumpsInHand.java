package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;

import java.util.Optional;

/**
 * {@code TrumpsInHand(x,y)}
 * tries to get the {@code x}th rank (234...QKA) of a trump in hand,
 * or the {@code y} node if this errors.
 */
public class TrumpsInHand extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        try {
            return context.getTrumpsRanks().get(children.get(0).evaluate(item, context).intValue());
        } catch (IndexOutOfBoundsException e) {
            return children.get(1).evaluate(item, context);
        }
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }
}
