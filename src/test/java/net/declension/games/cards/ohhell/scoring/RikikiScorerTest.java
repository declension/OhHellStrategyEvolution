package net.declension.games.cards.ohhell.scoring;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RikikiScorerTest {

    private RikikiScorer scorer;

    @Before
    public void setUp() {
        scorer = new RikikiScorer();
    }

    @Test
    public void scoreForShouldWork() {
        assertThat(scorer.scoreFor(0, 0)).isEqualTo(10);
        assertThat(scorer.scoreFor(0, 1)).isEqualTo(-2);
        assertThat(scorer.scoreFor(1, 0)).isEqualTo(-2);
        assertThat(scorer.scoreFor(1, 1)).isEqualTo(12);
        assertThat(scorer.scoreFor(3, 3)).isEqualTo(16);
    }
}