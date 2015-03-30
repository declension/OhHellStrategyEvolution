package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.evolution.OhHellStrategyCandidateFactory;
import net.declension.ea.cards.ohhell.evolution.TournamentPlayingEvolutionEngine;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;
import java.util.stream.IntStream;

public class OhHellStrategyEvolver {

    public static final int NATIVE_POPULATION_SIZE = 6;
    public static final int ELITE_COUNT = 1;
    public static final int MAX_RUNTIME_SECONDS = 60;
    public static final int GAMES_PER_TOURNAMENT = 30;
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolver.class);
    public static final Probability REPLACEMENT_PROBABILITY = new Probability(0.1);


    public static void main(String[] args) {
        // Create the engine
        int maxHandSize = 51 / (NATIVE_POPULATION_SIZE + TournamentPlayingEvolutionEngine.OUTSIDER_COUNT);
        LOGGER.debug("Maximum hand size={}", maxHandSize);
        GameSetup gameSetup = new GameSetup(() -> IntStream.rangeClosed(1, maxHandSize).boxed(), new StandardRules());

        EvolutionEngine<OhHellStrategy> engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT);

        engine.addEvolutionObserver(data -> LOGGER.info("Generation #{}, popn. {}. Fitness: mean={}, sd={}",
                                                        data.getGenerationNumber(), data.getPopulationSize(),
                                                        data.getMeanFitness(), data.getFitnessStandardDeviation()));

        // Go!
        List<EvaluatedCandidate<OhHellStrategy>> population
                = engine.evolvePopulation(NATIVE_POPULATION_SIZE, ELITE_COUNT, new ElapsedTime(MAX_RUNTIME_SECONDS * 1000));

        OhHellStrategy bestStrategy = population.get(0).getCandidate();
        LOGGER.warn("The best was {} with a score of {}: {}",
                    bestStrategy, population.get(0).getFitness(), bestStrategy.fullDetails());
    }

    public static TournamentPlayingEvolutionEngine createEngine(GameSetup gameSetup, int gamesPerTournament) {
        OhHellStrategyCandidateFactory candidateFactory = new OhHellStrategyCandidateFactory(gameSetup, 5);
        return new TournamentPlayingEvolutionEngine(
                gameSetup,
                candidateFactory,
                new Replacement<>(candidateFactory, REPLACEMENT_PROBABILITY),
                new SigmaScaling(),
                gamesPerTournament);
    }
}
