package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.evolution.*;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardOhHellRoundSizer;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.operators.SplitEvolution;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;

import static java.util.Arrays.asList;

public class OhHellStrategyEvolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolver.class);

    public static final int NATIVE_POPULATION_SIZE = 6;
    public static final int ELITE_COUNT = 1;
    public static final int MAX_RUNTIME_SECONDS = 60;
    public static final int GAMES_PER_TOURNAMENT = 30;
    public static final Probability REPLACEMENT_PROBABILITY = new Probability(0.1);
    public static final int MAX_BID_NODE_DEPTH = 6;
    public static final Probability SIMPLIFICATION_PROBABILITY = new Probability(0.1);


    public static void main(String[] args) {
        // Create the engine
        GameSetup gameSetup = defaultGameSetup();

        GeneticStrategyFactory candidateFactory = new GeneticStrategyFactory(gameSetup, MAX_BID_NODE_DEPTH);
        EvolutionEngine<GeneticStrategy> engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT,
                                                               createEvolution(candidateFactory,
                                                                               REPLACEMENT_PROBABILITY, Probability.ONE),
                                                               candidateFactory);

        engine.addEvolutionObserver(createLoggingObserver());
        // Go!
        List<EvaluatedCandidate<GeneticStrategy>> population
                = engine.evolvePopulation(NATIVE_POPULATION_SIZE, ELITE_COUNT,
                                          new ElapsedTime(MAX_RUNTIME_SECONDS * 1000));

        OhHellStrategy bestStrategy = population.get(0).getCandidate();
        LOGGER.warn("The best was {} with a score of {}: {}",
                    bestStrategy, population.get(0).getFitness(), bestStrategy.fullDetails());
    }

    public static GameSetup defaultGameSetup() {
        return new GameSetup(new StandardOhHellRoundSizer(), new StandardRules());
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

    public static EvolutionEngine<GeneticStrategy> createEngine(GameSetup gameSetup, int gamesPerTournament,
                                                                EvolutionaryOperator<GeneticStrategy> evolution,
                                                                CandidateFactory<GeneticStrategy> candidateFactory) {
        TournamentPlayingFitnessEvaluator tournamentPlayingFitnessEvaluator
                = new TournamentPlayingFitnessEvaluator(gameSetup, gamesPerTournament,
                                                        new AlgorithmicStrategyFactory(gameSetup));
        GenerationalEvolutionEngine<GeneticStrategy> engine
                = new GenerationalEvolutionEngine<>(candidateFactory,
                                                    evolution,
                                                    tournamentPlayingFitnessEvaluator,
                                                    new SigmaScaling(),
                                                    gameSetup.getRNG());
        //engine.setSingleThreaded(true);
        return engine;
    }

    public static EvolutionaryOperator<GeneticStrategy> createEvolution(GeneticStrategyFactory candidateFactory,
                                                                  Probability replacementProbability,
                                                                  Probability crossoverProbability) {
        return new EvolutionPipeline<>(
                asList(new Replacement<>(candidateFactory, replacementProbability),
                       new SplitEvolution<>(new TreeCrossover(crossoverProbability),
                                            new TreeMutation(Probability.ONE, candidateFactory.getBidNodeFactory()),
                                            0.7),
                       new Simplification(SIMPLIFICATION_PROBABILITY)));
    }
}
