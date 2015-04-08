package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.ea.cards.ohhell.nodes.Node;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static net.declension.games.cards.Suit.ALL_SUITS;

/**
 * {@code NonTrumpsInHand(suit, index, default)}
 * if possible, returns the #{@code index}th card rank (234...QKA) of an arbitrary non-trump suit #{@code suit} in
 * the player's hand,
 * or the {@code default} node if this errors.
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
            return defaultChild().evaluate(bid, context);
        }
    }

    private int listIndex(Range bid, BidEvaluationContext context) {
        return indexChild().evaluate(bid, context).intValue();
    }

    @Override
    public Node<Range, BidEvaluationContext> simplifiedVersion() {
        Node<Range, BidEvaluationContext> simpleSuitChild = suitChild().simplifiedVersion();
        Node<Range, BidEvaluationContext> simpleDefaultChild = defaultChild().simplifiedVersion();
        if (outOfBounds(simpleSuitChild, ALL_SUITS.size())) {
            return simpleDefaultChild;
        }
        // For efficiency, delay these simplifications if they don't matter.
        Node<Range, BidEvaluationContext> simpleIndexChild = indexChild().simplifiedVersion();
        if (outOfBounds(simpleIndexChild, MAX_CARDS_IN_HAND)) {
            return simpleDefaultChild;
        }
        Node<Range, BidEvaluationContext> newNode = shallowCopy();
        newNode.setChildren(asList(simpleSuitChild, simpleIndexChild, simpleDefaultChild));
        return newNode;
    }

    private Node<Range, BidEvaluationContext> suitChild() {
        return children.get(0);
    }

    private Node<Range, BidEvaluationContext> indexChild() {
        return children.get(1);
    }

    private Node<Range, BidEvaluationContext> defaultChild() {
        return children.get(2);
    }

    @Override
    public Node<Range, BidEvaluationContext> shallowCopy() {
        return new NonTrumpsInHand();
    }

    @Override
    public Optional<Integer> arity() {
        return Optional.of(3);
    }

    @Override
    public String toString() {
        return format("(NonTrumpsInHand[%s,%s] else %s)", suitChild(), indexChild(), defaultChild());
    }
}
