package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.games.cards.ohhell.GameSetup;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeneticStrategyFactoryTest {
    private GeneticStrategyFactory factory;
    private GameSetup gameSetup;
    private Random rng;

    @Before
    public void setUp() throws Exception {
        rng = mock(Random.class);
        gameSetup = mock(GameSetup.class);
        when(gameSetup.getRNG()).thenReturn(rng);
        factory = new GeneticStrategyFactory(gameSetup, 2);
    }

    @Test
    public void generateRandomCandidateShouldCreateRealisticObjects() {
        GeneticStrategy strat = factory.generateRandomCandidate(rng);
        assertThat(strat.getRng()).isEqualTo(rng);
        assertThat(strat.getBidEvaluator()).isNotNull();
    }

    @Test
    public void generateShouldReproduceEqualStrategyIfNoRandomness() {
        GeneticStrategy strat = factory.generateRandomCandidate(rng);
        // Un-random, so should produce the same results
        assertThat(factory.generateRandomCandidate(rng)).isEqualTo(strat);
    }
}