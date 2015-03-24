package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.pickRandomly;
import static net.declension.collections.CollectionUtils.rankEntrySetByIntValue;

/**
 * Allows players to compete in {@link Game}s against each other repeatedly.
 */
public class Tournament<T extends OhHellStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tournament.class);
    private final List<Player<T>> players;
    private final GameSetup gameSetup;

    public Tournament(List<Player<T>> players, GameSetup gameSetup) {
        this.players = players;
        this.gameSetup = gameSetup;
    }

    /**
     * Plays games, returns summed rankings.
     * @return a map of average rankings
     */
    public Map<Player<T>, Double> playMultipleGamesSequentially(int numberOfGames) {
        Map<Player, Double> totalRankings = IntStream.rangeClosed(1, numberOfGames).boxed()
                .flatMap(i -> {
                    List<Map.Entry<Player, Integer>> rankings = rankEntrySetByIntValue(createGame().play());
                    LOGGER.info("Rankings for this game: {}", rankings);
                    return rankings.stream();
                })
                .collect(toMap(Map.Entry::getKey,
                               e -> Double.valueOf(e.getValue()), (l, r) -> l + r));
        return totalRankings.entrySet().stream()
              .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue() / numberOfGames))
              .collect(toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private Game createGame() {
        return new Game(players, gameSetup, pickRandomly(gameSetup.getRNG(), players));
    }

}
