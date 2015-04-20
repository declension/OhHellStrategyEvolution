package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

import static java.lang.Double.NaN;
import static java.lang.String.format;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;
import static net.declension.games.cards.Rank.ALL_RANKS;

/**
 * {@code TrumpsInHand(x,y)}
 * tries to get the {@code x}th rank (234...QKA) of a trump in hand, returning a {@link Double#NaN} if out of bounds.
 */
public class TrumpsInHand extends BaseBiddingMethodNode {

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        try {
            int index = listIndex(item, context);
            if (index < 0 || index >= ALL_RANKS.size()) {
                return NaN;
            }
            return context.myTrumpsCardRanks().get(index);
        } catch (IndexOutOfBoundsException e) {
            return NaN;
        }
    }

    @Override
    public Node<Range, BidEvaluationContext> simplifiedVersion() {
        Node<Range, BidEvaluationContext> simpleIndexChild = child(0).simplifiedVersion();
        if (outOfBounds(simpleIndexChild, ALL_RANKS.size())) {
            return deadNumber();
        }
        Node<Range, BidEvaluationContext> newNode = shallowCopy();
        newNode.addChild(simpleIndexChild);
        return newNode;
    }

    private int listIndex(Range item, BidEvaluationContext context) {
        return child(0).evaluate(item, context).intValue();
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new TrumpsInHand();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(1);
    }

    @Override
    public String toString() {
        return format("(trumpsRank[#%s])", child(0));
    }
}
