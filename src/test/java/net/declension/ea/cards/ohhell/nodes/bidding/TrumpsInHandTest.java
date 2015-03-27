package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.games.cards.Rank;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.nodes.ConstNode.constant;
import static net.declension.games.cards.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrumpsInHandTest {

    public static final List<Rank> RANKS = asList(QUEEN, EIGHT, THREE);
    private static final List<RankRanking> TRUMPS = RANKS.stream()
                                                         .map(RankRanking::rankingOf)
                                                         .collect(toList());
    private TrumpsInHand node;
    private BidEvaluationContext bdd;
    private Range bid;

    @Before
    public void setUp() throws Exception {
        node = new TrumpsInHand();
        bid = mock(Range.class);
        bdd = mock(BidEvaluationContext.class);
        when(bdd.getTrumpsRanks()).thenReturn(TRUMPS);
    }

    @Test
    public void doEvaluationShouldChooseCorrectNode() {
        setNumericParams(1, 3);
        // Remember, TWO is low ie #1, (and this is one-indexed)
        assertThat(node.evaluate(bid, bdd)).isEqualTo(8 - 1);
    }

    @Test
    public void firstResultShouldBeFirstInUnderlyingList() {
        setNumericParams(0, 3);
        int expectedRank = asList(Rank.values()).indexOf(QUEEN) + 1;
        assertThat(node.evaluate(bid, bdd)).isEqualTo(expectedRank);
    }

    @Test
    public void doEvaluationShouldReturnSecondNodeIfOutOfBounds() {
        setNumericParams(99, 3);
        assertThat(node.evaluate(bid, bdd)).isEqualTo(3);

        setNumericParams(-1, 3);
        assertThat(node.evaluate(bid, bdd)).isEqualTo(3);
    }

    private Node<?,?> setNumericParams(int first, int second) {
        node.setChildren(asList(constant(first), constant(second)));
        return node;
    }
}