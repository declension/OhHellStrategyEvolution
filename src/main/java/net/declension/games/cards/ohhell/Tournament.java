package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
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
     * Plays games, returns summed rankings.
     * @return a map of summed rankings, whatever that really implies.
     */
    public Map<Player, Double> playMultipleGamesSequentially(int numberOfGames) {
        Game game = new Game(players, gameSetup, pickRandomly(gameSetup.getRNG(), players));
        return IntStream.rangeClosed(1, numberOfGames).boxed()
                .flatMap(i -> rankEntrySetByValue(game.play().entrySet()).stream())
                .collect(toMap(Map.Entry::getKey, e -> Double.valueOf(e.getValue()), (l,r) -> l + r));
    }

    private <T> List<Map.Entry<T, Integer>> rankEntrySetByValue(Collection<Map.Entry<T, Integer>> input) {
        List<Map.Entry<T, Integer>> list = input.stream().sequential()
                .sorted(comparing(Map.Entry::getValue, Integer::compare))
                .collect(toList());

        List<Map.Entry<T, Integer>> ret = new ArrayList<>();
        for (int i = 1; i < list.size() + 1; i++) {
            Map.Entry<T, Integer> item = list.get(i - 1);
            ret.add(new AbstractMap.SimpleImmutableEntry<>(item.getKey(), i));
        }
        return ret;
    }

}
