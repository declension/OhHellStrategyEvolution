package net.declension.ea.cards.ohhell;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.operators.IdentityOperator;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;
import java.util.stream.IntStream;

public class OhHellStrategyEvolver {

    public static final int POPULATION_SIZE = 8;
    public static final int ELITE_COUNT = 2;
    public static final int MAX_RUNTIME_SECONDS = 10;
    public static final int GAMES_PER_TOURNAMENT = 40;
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolver.class);


    public static void main(String[] args) {
        // Create the engine
        int maxHandSize = 51 / (POPULATION_SIZE + 1);
        LOGGER.debug("Maximum hand size={}", maxHandSize);
        GameSetup gameSetup = new GameSetup(() -> IntStream.rangeClosed(1, maxHandSize).boxed(), new StandardRules());


        EvolutionEngine<OhHellStrategy> engine = createEngine(gameSetup);

        engine.addEvolutionObserver(data -> LOGGER.info("Generation #{}, popn. {}. Fitness: mean={}, sd={}",
                                                        data.getGenerationNumber(), data.getPopulationSize(),
                                                        data.getMeanFitness(), data.getFitnessStandardDeviation()));

        // Go!
        List<EvaluatedCandidate<OhHellStrategy>> population
                = engine.evolvePopulation(POPULATION_SIZE, ELITE_COUNT, new ElapsedTime(MAX_RUNTIME_SECONDS * 1000));

        OhHellStrategy bestStrategy = population.get(0).getCandidate();
        LOGGER.warn("The best was {} with a score of {}.", bestStrategy, population.get(0).getFitness());
    }

    private static TournamentPlayingEvolutionEngine createEngine(GameSetup gameSetup) {
        return new TournamentPlayingEvolutionEngine(
                new OhHellStrategyCandidateFactory(),
                new IdentityOperator<>(),
                new StochasticUniversalSampling(),
                gameSetup, GAMES_PER_TOURNAMENT);
    }
}
