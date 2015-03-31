package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.evolution.GeneticStrategyCandidateFactory;
import net.declension.ea.cards.ohhell.evolution.NodeMutation;
import net.declension.ea.cards.ohhell.evolution.TournamentPlayingEvolutionEngine;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class OhHellStrategyEvolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolver.class);

    public static final int NATIVE_POPULATION_SIZE = 6;
    public static final int ELITE_COUNT = 1;
    public static final int MAX_RUNTIME_SECONDS = 60;
    public static final int GAMES_PER_TOURNAMENT = 30;
    public static final Probability REPLACEMENT_PROBABILITY = new Probability(0.05);
    public static final Probability MUTATION_PROBABILITY = new Probability(0.2);
    public static final Probability NODE_MUTATION_PROBABILITY = new Probability(0.1);
    public static final int BID_NODE_DEPTH = 4;


    public static void main(String[] args) {
        // Create the engine
        int maxHandSize = 51 / (NATIVE_POPULATION_SIZE + TournamentPlayingEvolutionEngine.OUTSIDER_COUNT);
        LOGGER.debug("Maximum hand size={}", maxHandSize);
        GameSetup gameSetup = new GameSetup(() -> IntStream.rangeClosed(1, maxHandSize).boxed(), new StandardRules());

        EvolutionEngine<GeneticStrategy> engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT);

        engine.addEvolutionObserver(createLoggingObserver());
        // Go!
        List<EvaluatedCandidate<GeneticStrategy>> population
                = engine.evolvePopulation(NATIVE_POPULATION_SIZE, ELITE_COUNT,
                                          new ElapsedTime(MAX_RUNTIME_SECONDS * 1000));

        OhHellStrategy bestStrategy = population.get(0).getCandidate();
        LOGGER.warn("The best was {} with a score of {}: {}",
                    bestStrategy, population.get(0).getFitness(), bestStrategy.fullDetails());
    }

    public static EvolutionObserver<GeneticStrategy> createLoggingObserver() {
        return data -> {
            GeneticStrategy best = data.getBestCandidate();
            LOGGER.info("Generation #{}, popn. {}. Fitness: mean={}, sd={}. Best: {} ({}) with {}",
                        data.getGenerationNumber(), data.getPopulationSize(),
                        data.getMeanFitness(), data.getFitnessStandardDeviation(),
                        best, data.getBestCandidateFitness(), best.bidEvaluator);
        };
    }

    public static TournamentPlayingEvolutionEngine createEngine(GameSetup gameSetup, int gamesPerTournament) {
        GeneticStrategyCandidateFactory candidateFactory
                = new GeneticStrategyCandidateFactory(gameSetup, BID_NODE_DEPTH);


        List<EvolutionaryOperator<GeneticStrategy>> evolutionaryOperators
                = asList(new Replacement<>(candidateFactory, REPLACEMENT_PROBABILITY),
                         new NodeMutation(MUTATION_PROBABILITY, NODE_MUTATION_PROBABILITY));
        EvolutionaryOperator<GeneticStrategy> evolution = new EvolutionPipeline<>(evolutionaryOperators);
        return new TournamentPlayingEvolutionEngine(
                gameSetup,
                candidateFactory,
                evolution,
                new SigmaScaling(),
                gamesPerTournament);
    }
}
