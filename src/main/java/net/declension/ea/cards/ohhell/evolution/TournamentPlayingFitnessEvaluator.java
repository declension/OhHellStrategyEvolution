package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Tournament;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static net.declension.utils.Validation.requireNonNullParam;

/**
 * Plays a single (currently) genetic strategy against {@link #OUTSIDER_COUNT} other algorithmic strategies
 * by having them play a Tournament of a configurable number of games.
 *
 */
public class TournamentPlayingFitnessEvaluator implements FitnessEvaluator<GeneticStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentPlayingFitnessEvaluator.class);
    public static final int OUTSIDER_COUNT = 5;

    private final GameSetup gameSetup;
    private final int numberOfGames;
    private final AlgorithmicStrategyFactory outsiderFactory;

    public TournamentPlayingFitnessEvaluator(GameSetup gameSetup, int numberOfGames,
                                             AlgorithmicStrategyFactory outsiderFactory) {
        requireNonNullParam(gameSetup, "Game Setup");
        requireNonNullParam(outsiderFactory, "Outsider Factory");
        this.gameSetup = gameSetup;
        this.numberOfGames = numberOfGames;
        this.outsiderFactory = outsiderFactory;
    }

    @Override
    public double getFitness(GeneticStrategy candidate, List<? extends GeneticStrategy> population) {
        requireNonNullParam(candidate, "Candidate");
        List<OhHellStrategy> outsiders = outsiderFactory.createStrategies(OUTSIDER_COUNT);
        List<Player> players = createPlayers(singletonList(candidate), outsiders);
        Tournament tournament = new Tournament(players, gameSetup);

        Map<Player, Double> results = tournament.playMultipleGamesSequentially(numberOfGames);
        LOGGER.debug("Final scores: {}", prettyPrint(results.entrySet()));
        return results.entrySet().stream().filter(e -> e.getKey().getStrategy().equals(candidate))
                                          .findFirst()
                                          .get().getValue();
    }

    @Override
    public boolean isNatural() {
        return true;
    }

    private String prettyPrint(Set<Map.Entry<Player, Double>> evaluated) {
        return evaluated.stream()
                        .map(e -> format("%s: %.2f",  e.getKey(), e.getValue()))
                        .collect(toList())
                        .toString();
    }


    private List<Player> createPlayers(List<? extends GeneticStrategy> population, List<OhHellStrategy> outsiders) {
        List<OhHellStrategy> totalPopulation = new ArrayList<>(population);
        totalPopulation.addAll(outsiders);
        return totalPopulation.stream()
                              .map(s -> new BasicPlayer(gameSetup, s)).collect(toList());
    }
}
