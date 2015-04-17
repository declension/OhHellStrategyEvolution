package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.pickRandomly;
import static net.declension.collections.CollectionUtils.rankEntrySetByIntValue;

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
     * Plays games, returns average rankings.
     * @return a map of average rankings
     */
    public Map<Player, Double> playMultipleGamesSequentially(int numberOfGames) {
        Map<Player, Integer> totalRankings = IntStream.rangeClosed(1, numberOfGames).boxed()
                .flatMap(i -> {
                    List<Map.Entry<Player, Integer>> rankings = rankEntrySetByIntValue(createGame().play());
                    LOGGER.debug("Rankings for this game: {}", rankings);
                    return rankings.stream();
                })
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l + r));
        Map<Player, Double> averageRankings = totalRankings.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue))
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), (double) e.getValue() / numberOfGames))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, LinkedHashMap::new));
        LOGGER.info("Average rankings for {}-player tournament of {} game(s): {}",
                    players.size(), numberOfGames, prettyResults(averageRankings));
        return averageRankings;
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
