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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TournamentPlayingEvolutionEngine extends GenerationalEvolutionEngine<GeneticStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentPlayingEvolutionEngine.class);
    public static final int OUTSIDER_COUNT = 3;

    private final GameSetup gameSetup;
    private final int numberOfGames;
    private AlgorithmicStrategyFactory outsiderFactory;

    /**
     * Create a tournament-playing engine. This creates players based on strategies created using the {@code
     * candidateFactory}.
     *  @param gameSetup The setup for each game
     * @param candidateFactory a factory to create new candidates, either initially or later
     *                         ({@see org.uncommons.watchmaker.framework.operators.Replacement}).
     * @param outsiderFactory
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
        List<OhHellStrategy> outsiders = outsiderFactory.createStrategies(OUTSIDER_COUNT);
        List<Player> players = createPlayers(population, outsiders);
        Tournament tournament = new Tournament(players, gameSetup);

        // TODO: allow big populations by sharding into separate tournaments, running in parallel, then collating
        // results. Somehow.
        Map<Player, Double> results = tournament.playMultipleGamesSequentially(numberOfGames);
        return results.entrySet().stream()
                .filter(e -> !outsiders.contains(e.getKey().getStrategy()))
                .map(e -> new EvaluatedCandidate<>((GeneticStrategy) e.getKey().getStrategy(), e.getValue()))
                .collect(toList());
    }

    private List<Player> createPlayers(List<GeneticStrategy> population, List<OhHellStrategy> outsiders) {
        List<OhHellStrategy> totalPopulation = new ArrayList<>(population);
        totalPopulation.addAll(outsiders);
        return totalPopulation.stream()
                              .map(s -> new BasicPlayer(gameSetup, s)).collect(toList());
    }

}
