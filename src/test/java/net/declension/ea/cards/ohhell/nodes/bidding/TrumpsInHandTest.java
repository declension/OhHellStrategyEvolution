package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.UnaryNode;
import net.declension.games.cards.Rank;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ABS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static net.declension.games.cards.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrumpsInHandTest {

    public static final List<Rank> RANKS = asList(QUEEN, EIGHT, THREE);
    private static final List<RankRanking> TRUMPS = RANKS.stream()
                                                         .map(RankRanking::rankingOf)
                                                         .collect(toList());
    private static final int DEFAULT_VALUE = 999;
    public static final UnaryNode<Range, BidEvaluationContext>
            EFFECTIVELY_FOUR = unary(ABS, constant(-4));
    private TrumpsInHand node;
    private BidEvaluationContext context;
    private Range bid;

    @Before
    public void setUp() throws Exception {
        node = new TrumpsInHand();
        bid = mock(Range.class);
        context = mock(BidEvaluationContext.class);
        when(context.myTrumpsCardRanks()).thenReturn(TRUMPS);
    }

    @Test
    public void doEvaluationShouldChooseCorrectNode() {
        setNumericParams(1, 3);
        // Remember, TWO is low ie #1, (and this is one-indexed)
        assertThat(node.evaluate(bid, context)).isEqualTo(8 - 1);
    }

    @Test
    public void firstResultShouldBeFirstInUnderlyingList() {
        setNumericParams(0, 3);
        int expectedRank = asList(Rank.values()).indexOf(QUEEN) + 1;
        assertThat(node.evaluate(bid, context)).isEqualTo(expectedRank);
    }

    @Test
    public void doEvaluationShouldReturnSecondNodeIfOutOfBounds() {
        setNumericParams(99, 3);
        assertThat(node.evaluate(bid, context)).isEqualTo(3);

        setNumericParams(-1, 3);
        assertThat(node.evaluate(bid, context)).isEqualTo(3);
    }

    @Test
    public void simplifyShouldReturnDefaultIfIndexOutOfRange() {
        setNumericParams(52, DEFAULT_VALUE);
        assertThat(node.simplifiedVersion()).isEqualTo(constant(DEFAULT_VALUE));
    }

    @Test
    public void simplifyShouldReturnOtherConstantIfAllGood() {
        setNumericParams(11, DEFAULT_VALUE);
        assertThat(node.simplifiedVersion()).isNotEqualTo(constant(DEFAULT_VALUE));
    }

    @Test
    public void simplifyShouldReturnSimplifiedTreeIfOutside() {
        node.addChild(constant(999));
        node.addChild(EFFECTIVELY_FOUR);
        assertThat(node.simplifiedVersion()).isEqualTo(constant(4));
    }

    @Test
    public void simplifyShouldReturnNodeWithSimplifiedChildrenIfInside() {
        node.addChild(EFFECTIVELY_FOUR);
        node.addChild(unary(ABS, constant(-1234)));
        Node<Range, BidEvaluationContext> simple = node.simplifiedVersion();
        assertThat(simple.child(0)).isEqualTo(constant(4));
        assertThat(simple.child(1)).isEqualTo(constant(1234));
    }

    @Test
    public void shouldNotBeConstant() {
        assertThat(node.effectivelyConstant()).isFalse();
    }

    private Node<?,?> setNumericParams(int first, int second) {
        node.setChildren(asList(constant(first), constant(second)));
        return node;
    }
}