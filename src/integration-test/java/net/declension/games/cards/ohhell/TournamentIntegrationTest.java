package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.AverageStrategy;
import net.declension.games.cards.ohhell.strategy.RandomStrategy;
import net.declension.games.cards.ohhell.strategy.SimpleStrategy;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TournamentIntegrationTest extends BaseGameTest {

    private static final int NUM_PLAYERS = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentIntegrationTest.class);
    private static final int MAX_HAND_SIZE = 6;
    public static final int NUM_GAMES = 30;

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup(MAX_HAND_SIZE);
        players = new ArrayList<>();
        players.addAll(generatePlayers(NUM_PLAYERS, new AverageStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(NUM_PLAYERS, new RandomStrategy(gameSetup.getRNG()), gameSetup));
        //players.addAll(generatePlayers(NUM_PLAYERS, new TrumpsBasedRandomStrategy(gameSetup), gameSetup));
        players.addAll(generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup), gameSetup));
    }

    @Test
    public void playLotsOfGamesShould() {
        Map<Player, Double> stats = new Tournament(players, gameSetup).playMultipleGamesSequentially(NUM_GAMES);
        List<String> sortedStats = stats.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Double::compare))
                .map(entry -> format("%.2f for %s", entry.getValue() / NUM_GAMES, entry.getKey()))
                .collect(toList());
        LOGGER.warn("Average rankings after {} games: {}", NUM_GAMES, sortedStats);
    }
}