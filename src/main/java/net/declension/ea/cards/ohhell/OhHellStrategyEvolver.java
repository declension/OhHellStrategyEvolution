package net.declension.ea.cards.ohhell;

import net.declension.ea.cards.ohhell.evolution.GeneticStrategyFactory;
import net.declension.ea.cards.ohhell.evolution.TreeMutation;
import net.declension.ea.cards.ohhell.evolution.Simplification;
import net.declension.ea.cards.ohhell.evolution.TournamentPlayingEvolutionEngine;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.ElapsedTime;

import java.util.List;

import static java.util.Arrays.asList;
import static net.declension.games.cards.ohhell.GameSetup.standardOhHellHandSizeSequence;

public class OhHellStrategyEvolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolver.class);

    public static final int NATIVE_POPULATION_SIZE = 6;
    public static final int ELITE_COUNT = 1;
    public static final int MAX_RUNTIME_SECONDS = 60;
    public static final int GAMES_PER_TOURNAMENT = 30;
    public static final Probability REPLACEMENT_PROBABILITY = new Probability(0.1);
    public static final Probability MUTATION_PROBABILITY = new Probability(0.2);
    public static final Probability NODE_MUTATION_PROBABILITY = new Probability(0.1);
    public static final int BID_NODE_DEPTH = 5;
    public static final Probability SIMPLIFICATION_PROBABILITY = new Probability(0.1);


    public static void main(String[] args) {
        // Create the engine
        GameSetup gameSetup = createGameSetup(NATIVE_POPULATION_SIZE + TournamentPlayingEvolutionEngine.OUTSIDER_COUNT);

        GeneticStrategyFactory candidateFactory = new GeneticStrategyFactory(gameSetup, BID_NODE_DEPTH);
        EvolutionEngine<GeneticStrategy> engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT,
                                                               createDefaultEvolutionaryOperators(candidateFactory),
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

    public static GameSetup createGameSetup(int numPlayers) {
        StandardRules rules = new StandardRules();
        return new GameSetup(standardOhHellHandSizeSequence(rules.maximumCardsFor(numPlayers))::stream, rules);
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

    public static TournamentPlayingEvolutionEngine createEngine(GameSetup gameSetup, int gamesPerTournament,
                                                                EvolutionaryOperator<GeneticStrategy> evolution,
                                                                CandidateFactory<GeneticStrategy> candidateFactory) {
        return new TournamentPlayingEvolutionEngine(
                gameSetup,
                candidateFactory,
                evolution,
                new SigmaScaling(),
                gamesPerTournament);
    }

    public static EvolutionaryOperator<GeneticStrategy> createDefaultEvolutionaryOperators(
            GeneticStrategyFactory candidateFactory) {
        return new EvolutionPipeline<>(
                asList(new Replacement<>(candidateFactory, REPLACEMENT_PROBABILITY),
                       new TreeMutation(MUTATION_PROBABILITY, NODE_MUTATION_PROBABILITY),
                       new Simplification(SIMPLIFICATION_PROBABILITY)));
    }
}
