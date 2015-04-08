package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.games.cards.Rank;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.data.Aggregator.MAX;
import static net.declension.ea.cards.ohhell.data.Aggregator.SUM;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.games.cards.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregatedBiddingDataTest {

    private BidEvaluationContext context;
    public static final List<Rank> RANKS = asList(QUEEN, EIGHT, THREE);
    private static final List<RankRanking> TRUMPS = RANKS.stream()
                                                         .map(RankRanking::rankingOf)
                                                         .collect(toList());
    private Range bid;

    @Before
    public void setUp() throws Exception {

        bid = mock(Range.class);
        context = mock(BidEvaluationContext.class);
        when(context.myTrumpsCardRanks()).thenReturn(TRUMPS);
        when(context.myOtherSuitsCardRanks()).thenReturn(
                asList(TRUMPS, emptyList(), asList(RankRanking.rankingOf(FOUR)), emptyList()));
    }

    @Test
    public void doEvaluationShouldPickTheRightList() {
        AggregatedBiddingData node = new AggregatedBiddingData(SUM);
        node.addChild(constant(3));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(4.0 - 1);
    }

    @Test
    public void doEvaluationShouldPickTheRightListAgain() {
        AggregatedBiddingData node = new AggregatedBiddingData(SUM);
        node.addChild(constant(0));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(11.0 + 7.0 + 2.0);
    }

    @Test
    public void doEvaluationShouldWorkWithMaxToo() {
        AggregatedBiddingData node = new AggregatedBiddingData(MAX);
        node.addChild(constant(0));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(11.0);
    }

    @Test
    public void doEvaluationShouldNotThrow() {
        AggregatedBiddingData node = new AggregatedBiddingData(MAX);
        node.addChild(constant(999));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(Double.NaN);
    }
}