package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.pickRandomly;

/**
 * Allows players to compete in {@link Game}s against each other repeatedly.
 */
public class Tournament {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tournament.class);
    private final List<Player> players;
    private final GameSetup gameSetup;

    public Tournament(List<Player> players, GameSetup gameSetup) {
        this.players = players;
        this.gameSetup = gameSetup;
    }

    /**
     * Plays games, returns sliglhtly normalised average scores.
     * @return a map of normalised average scores
     */
    public Map<Player, Double> playMultipleGamesSequentially(int numberOfGames) {
        Map<Player, Integer> totals = IntStream.rangeClosed(1, numberOfGames).boxed()
                .flatMap(i -> createGame().play().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l + r));
        IntSummaryStatistics summary = totals.values().stream().mapToInt(Integer::valueOf).summaryStatistics();
        LOGGER.info("Total scores for {}-player tournament of {} game(s): {}. Stats: {}",
                    players.size(), numberOfGames, totals, summary);

        Map<Player, Double> normalisedScores = totals.entrySet().stream()
                // Highest score first, for viewing.
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), normaliseScore(numberOfGames, e)))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, LinkedHashMap::new));
        LOGGER.info("Normalised scores for {}-player tournament of {} game(s): {}",
                    players.size(), numberOfGames, prettyResults(normalisedScores));
        return normalisedScores;
    }

    private double normaliseScore(int numberOfGames,Map.Entry<Player, Integer> e) {
        return Math.max(0, e.getValue() / numberOfGames + 300 / (4 + players.size()));
    }

    private List<String> prettyResults(Map<Player, Double> averageRankings) {
        return averageRankings.entrySet().stream()
                                         .map(e -> format("%s (%.2f)", e.getKey(), e.getValue()))
                                         .collect(toList());
    }

    private Game createGame() {
        return new Game(players, gameSetup, pickRandomly(gameSetup.getRNG(), players));
    }

}
