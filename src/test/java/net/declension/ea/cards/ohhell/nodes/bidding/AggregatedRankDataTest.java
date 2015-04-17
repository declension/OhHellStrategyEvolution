package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import net.declension.games.cards.Rank;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.data.Aggregator.*;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.bidding.AggregatedRankData.aggregatedRankData;
import static net.declension.games.cards.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregatedRankDataTest {

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
        AggregatedRankData node = new AggregatedRankData(SUM);
        node.addChild(constant(3));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(4.0 - 1);
    }

    @Test
    public void doEvaluationShouldPickTheRightListAgain() {
        AggregatedRankData node = new AggregatedRankData(SUM);
        node.addChild(constant(0));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(11.0 + 7.0 + 2.0);
    }

    @Test
    public void doEvaluationShouldWorkWithMaxToo() {
        AggregatedRankData node = new AggregatedRankData(MAX);
        node.addChild(constant(0));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(11.0);
    }

    @Test
    public void doEvaluationShouldNotThrow() {
        AggregatedRankData node = new AggregatedRankData(MAX);
        node.addChild(constant(999));
        assertThat(node.doEvaluation(bid, context)).isEqualTo(Double.NaN);
    }

    @Test
    public void simplifyShouldReplaceWithIntegerConstants() {
        assertThat(aggregatedRankData(COUNT, constant(1.5)).simplifiedVersion())
                  .isEqualTo(aggregatedRankData(COUNT, constant(1)));
    }

    @Test
    public void equalsShouldWork() {
        AggregatedRankData node = aggregatedRankData(MAX, constant(2));
        assertThat(node).isEqualTo(aggregatedRankData(MAX, constant(2)));
        assertThat(node).isNotEqualTo(aggregatedRankData(MAX, constant(3)));
        assertThat(node).isNotEqualTo(aggregatedRankData(MIN, constant(2)));
    }

    @Test
    public void mutateShouldChangeAggregator() {
        AggregatedRankData node = aggregatedRankData(MAX, constant(2));
        assertThat(node.mutate(new Random())).isNotEqualTo(aggregatedRankData(MAX, constant(2)));
    }
}