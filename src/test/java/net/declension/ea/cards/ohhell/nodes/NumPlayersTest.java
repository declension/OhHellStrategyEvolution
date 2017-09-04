package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.data.RankRanking;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NumPlayersTest {

    private NumPlayers<Range, InGameEvaluationContext> node;
    private Range mockRange;

    @Before
    public void setUp() {
        node = new NumPlayers<>();
        mockRange = mock(Range.class);
    }

    @Test
    public void doEvaluationShouldJustAccessTheNumPlayersInTheContext() {
        Range numPlayers = new Range(3, 1, 10);
        InGameEvaluationContext context = new NumPlayersOnlyEvaluationContext(numPlayers);
        Number result = node.doEvaluation(mockRange, context);
        assertThat(result.intValue()).isEqualTo(numPlayers.intValue());
        verifyZeroInteractions(mockRange);
    }

    @Test
    public void simplifyShouldDoNothing() {
        assertThat(node.simplifiedVersion()).isEqualTo(node);
    }


    private static class NumPlayersOnlyEvaluationContext implements InGameEvaluationContext {
        private final Range numPlayers;

        private NumPlayersOnlyEvaluationContext(Range numPlayers) {
            this.numPlayers = numPlayers;
        }

        @Override
        public Range handSize() {
            return null;
        }

        @Override
        public Range numPlayers() {
            return numPlayers;
        }

        @Override
        public List<RankRanking> myTrumpsCardRanks() {
            return null;
        }

        @Override
        public List<List<RankRanking>> myOtherSuitsCardRanks() {
            return null;
        }
    }
}
