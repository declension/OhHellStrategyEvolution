package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.Optional;

import static java.lang.String.format;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;

/**
 * {@code TrumpsInHand(x,y)}
 * tries to get the {@code x}th rank (234...QKA) of a trump in hand,
 * or the {@code y} node if this errors.
 */
public class TrumpsInHand extends BaseBiddingMethodNode {
    static final int MAX_CARDS_IN_HAND = 26;

    @Override
    protected Number doEvaluation(Range item, BidEvaluationContext context) {
        try {
            return context.myTrumpsCardRanks().get(listIndex(item, context));
        } catch (IndexOutOfBoundsException e) {
            return Double.NaN;
        }
    }

    @Override
    public Node<Range, BidEvaluationContext> simplifiedVersion() {
        Node<Range, BidEvaluationContext> simpleIndexChild = child(0).simplifiedVersion();
        if (outOfBounds(simpleIndexChild, MAX_CARDS_IN_HAND)) {
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
