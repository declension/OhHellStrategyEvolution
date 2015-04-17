package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Tournament;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.*;

import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TournamentPlayingEvolutionEngine extends GenerationalEvolutionEngine<GeneticStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentPlayingEvolutionEngine.class);
    public static final int OUTSIDER_COUNT = 5;
    public static final int CANDIDATES_PER_TOURNAMENT = 2;

    private final GameSetup gameSetup;
    private final int numberOfGames;
    private AlgorithmicStrategyFactory outsiderFactory;

    /**
     * Create a tournament-playing engine. This creates players based on strategies created using the {@code
     * candidateFactory}.
     *  @param gameSetup The setup for each game
     * @param candidateFactory a factory to create new candidates, either initially or later
     *                         ({@see org.uncommons.watchmaker.framework.operators.Replacement}).
     * @param outsiderFactory a factory for creating <strong>non-</strong>genetic candidates to play against.
     * @param evolutionScheme The method by which to evolve the population
     * @param selectionStrategy The selection to choose the fittest candidates
     * @param numberOfGames the number of games to play in each tournament.
     */
    public TournamentPlayingEvolutionEngine(GameSetup gameSetup,
                                            CandidateFactory<GeneticStrategy> candidateFactory,
                                            AlgorithmicStrategyFactory outsiderFactory,
                                            EvolutionaryOperator<GeneticStrategy> evolutionScheme,
                                            SelectionStrategy<? super OhHellStrategy> selectionStrategy,
                                            int numberOfGames) {
        super(candidateFactory, evolutionScheme, new PreComputedFitnessEvaluator<>(), selectionStrategy,
              gameSetup.getRNG());
        this.gameSetup = gameSetup;
        this.outsiderFactory = outsiderFactory;
        this.numberOfGames = numberOfGames;
    }

    @Override
    protected List<EvaluatedCandidate<GeneticStrategy>> evaluatePopulation(List<GeneticStrategy> population) {
        int start = 0;
        List<EvaluatedCandidate<GeneticStrategy>> evaluated = new ArrayList<>();
        while (population.size() - 1 > start) {
            int end = Math.min(start + CANDIDATES_PER_TOURNAMENT, population.size());
            LOGGER.debug("Sub-population from {} to {}", start, end);
            List<GeneticStrategy> subPopulation = population.subList(start, end);
            List<OhHellStrategy> outsiders = outsiderFactory.createStrategies(OUTSIDER_COUNT);
            List<Player> players = createPlayers(subPopulation, outsiders);
            Tournament tournament = new Tournament(players, gameSetup);

            Map<Player, Double> results = tournament.playMultipleGamesSequentially(numberOfGames);
            evaluated.addAll(
                    results.entrySet().stream()
                           .filter(e -> !outsiders.contains(e.getKey().getStrategy()))
                           .map(e -> new EvaluatedCandidate<>((GeneticStrategy) e.getKey().getStrategy(),
                                                              e.getValue()))
                           .collect(toList()));
            start += CANDIDATES_PER_TOURNAMENT;
        }
        evaluated.sort(Comparator.<EvaluatedCandidate, Double>comparing(EvaluatedCandidate::getFitness).reversed());
        LOGGER.info("Final scores: {}", prettyPrint(evaluated));
        return evaluated;
    }

    private String prettyPrint(List<EvaluatedCandidate<GeneticStrategy>> evaluated) {
        return evaluated.stream()
                        .map(e -> format("%s: %.2f",  e.getCandidate(), e.getFitness()))
                        .collect(toList())
                        .toString();
    }


    private List<Player> createPlayers(List<GeneticStrategy> population, List<OhHellStrategy> outsiders) {
        List<OhHellStrategy> totalPopulation = new ArrayList<>(population);
        totalPopulation.addAll(outsiders);
        return totalPopulation.stream()
                              .map(s -> new BasicPlayer(gameSetup, s)).collect(toList());
    }

}
