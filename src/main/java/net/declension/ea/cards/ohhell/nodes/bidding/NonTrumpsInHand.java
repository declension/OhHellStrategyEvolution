package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.games.cards.Suit.ALL_SUITS;

/**
 * {@code NonTrumpsInHand(suit, index, default)}
 * if possible, returns the #{@code index}th card rank (234...QKA) of an arbitrary non-trump suit #{@code suit} in
 * the player's hand, or {@code Double#NaN} node if this errors.
 */
public class NonTrumpsInHand extends BaseBiddingMethodNode {

    private static final int MAX_CARDS_IN_HAND = 52;

    @Override
    protected Number doEvaluation(Range bid, BidEvaluationContext context) {
        try {
            int suitIndex = suitChild().evaluate(bid, context).intValue();
            List<RankRanking> ranks = context.myOtherSuitsCardRanks().get(suitIndex);
            return ranks.get(listIndex(bid, context));
        } catch (IndexOutOfBoundsException e) {
            return Double.NaN;
        }
    }

    private int listIndex(Range bid, BidEvaluationContext context) {
        return indexChild().evaluate(bid, context).intValue();
    }

    @Override
    public Node<Range, BidEvaluationContext> simplifiedVersion() {
        Node<Range, BidEvaluationContext> simpleSuitChild = suitChild().simplifiedVersion();
        if (outOfBounds(simpleSuitChild, ALL_SUITS.size())) {
            return constant(Double.NaN);
        }
        // For efficiency, delay these simplifications if they don't matter.
        Node<Range, BidEvaluationContext> simpleIndexChild = indexChild().simplifiedVersion();
        if (outOfBounds(simpleIndexChild, MAX_CARDS_IN_HAND)) {
            return constant(Double.NaN);
        }
        Node<Range, BidEvaluationContext> newNode = shallowCopy();
        newNode.setChildren(asList(simpleSuitChild, simpleIndexChild));
        return newNode;
    }

    private Node<Range, BidEvaluationContext> suitChild() {
        return children.get(0);
    }

    private Node<Range, BidEvaluationContext> indexChild() {
        return children.get(1);
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new NonTrumpsInHand();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(2);
    }

    @Override
    public String toString() {
        return format("(nonTrumpsRank[suit #%s, card #%s])", suitChild(), indexChild());
    }
}
