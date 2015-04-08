package net.declension.ea.cards.ohhell;

import net.declension.games.cards.ohhell.GameSetup;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeneticStrategyTest {

    private GameSetup gameSetup;
    private Random rng;

    @Before
    public void setUp() {
        gameSetup = mock(GameSetup.class);
        rng = mock(Random.class);
        when(gameSetup.getRNG()).thenReturn(rng);
    }

    @Test
    public void getRngShouldReturnGameSetupOne() {
        GeneticStrategy strat = constantNodeStrategy(3);
        assertThat(strat.getRng()).isEqualTo(rng);
    }

    @Test
    public void equalsHappyPath() {
        GeneticStrategy strat = constantNodeStrategy(3);
        assertThat(strat).isEqualTo(strat);
        assertThat(strat).isEqualTo(constantNodeStrategy(3));
    }

    @Test
    public void equalsUnhappyPath() {
        GeneticStrategy strat = constantNodeStrategy(3);
        assertThat(strat).isNotEqualTo(new GeneticStrategy(mock(GameSetup.class), constant(3)));
        assertThat(strat).isNotEqualTo(null);
        assertThat(strat).isNotEqualTo(constantNodeStrategy(5));
        assertThat(strat).isNotEqualTo("foo");
    }

    @Test
    public void hashCodeShouldBeDifferentBasedOn() {
        assertThat(constantNodeStrategy(3).hashCode()).isNotEqualTo(constantNodeStrategy(5).hashCode());
    }

    @Test
    public void hashCodeShouldBeEqualForEqualNodesAndGameSetup() {
        assertThat(constantNodeStrategy(3).hashCode()).isEqualTo(constantNodeStrategy(3).hashCode());
    }

    private GeneticStrategy constantNodeStrategy(int value) {
        return new GeneticStrategy(gameSetup, constant(value));
    }
}