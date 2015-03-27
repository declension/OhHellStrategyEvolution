package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;

import java.util.List;
import java.util.Optional;

/**
 * {@code NonTrumpsInHand(suit, index, default)}
 * if possible, returns the #{@code index}th card rank (234...QKA) of an arbitrary non-trump suit #{@code suit} in
 * the player's hand,
 * or the {@code default} node if this errors.
 */
public class NonTrumpsInHand extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(Range bid, BidEvaluationContext context) {
        try {
            int suitIndex = children.get(0).evaluate(bid, context).intValue();
            List<RankRanking> ranks = context.getOtherRanks().get(suitIndex);
            int index = children.get(1).evaluate(bid, context).intValue();
            return ranks.get(index);

        } catch (IndexOutOfBoundsException e) {
            return children.get(2).evaluate(bid, context);
        }
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(3);
    }

    @Override
    public String toString() {
        return "NonTrumpsInHand{}";
    }
}
