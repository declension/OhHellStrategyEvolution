package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.evolution.TournamentPlayingEvolutionEngine;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.junit.Before;
import org.junit.Test;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;
import java.util.stream.IntStream;

import static net.declension.ea.cards.ohhell.OhHellStrategyEvolver.createEngine;
import static org.assertj.core.api.Assertions.assertThat;

public class OhHellStrategyEvolverTest {

    public static final int GAMES_PER_TOURNAMENT = 10;
    public static final int POPULATION_SIZE = 6;
    private static final int MAX_RUNTIME_SECONDS = 2;
    private GameSetup gameSetup;

    @Before
    public void setUp() throws Exception {
        int maxHandSize = 51 / (POPULATION_SIZE + 2);
        gameSetup = new GameSetup(() -> IntStream.rangeClosed(1, maxHandSize).boxed(), new StandardRules());
    }

    @Test
    public void createEngineIntegrationTest() {
        TournamentPlayingEvolutionEngine engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT);
        List<EvaluatedCandidate<OhHellStrategy>>
                population = engine.evolvePopulation(POPULATION_SIZE, 1, new ElapsedTime(MAX_RUNTIME_SECONDS * 1000));
        assertThat(population).hasSize(POPULATION_SIZE);
    }

}