package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.BaseIntegrationTest;
import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class GeneticStrategyFactoryIntegrationTest extends BaseIntegrationTest {

    private GeneticStrategyFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new GeneticStrategyFactory(gameSetup, 4);
        gameSetup = createDefaultGameSetup(MAX_HAND_SIZE);
    }

    @Test
    public void generateRandomCandidateShouldWork() {
        List<GeneticStrategy> population = factory
                .generateInitialPopulation(50, new ArrayList<>(), gameSetup.getRNG());
        assertThat(population).hasSize(50);
        population.stream().map(OhHellStrategy::fullDetails)
                  .peek(logger::info)
                  .collect(toList());
    }
}