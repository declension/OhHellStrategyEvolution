package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static net.declension.collections.CollectionUtils.ADD_NULLABLE_INTEGERS;
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
     * Plays games, returns scores
     * @return
     */
    public Map<Player, Integer> playLotsOfGames(int numberOfGames) {
        Game game = new Game(players, gameSetup, pickRandomly(gameSetup.getRNG(), players));
        return IntStream.rangeClosed(1, numberOfGames).boxed()
                .flatMap(i -> rankPlayers(game))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, ADD_NULLABLE_INTEGERS));
    }

    private Stream<Map.Entry<Player, Integer>> rankPlayers(Game game) {
        return game.play().entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, Integer::compare))
                .peek(entry -> LOGGER.debug(entry.toString()));
    }


}
