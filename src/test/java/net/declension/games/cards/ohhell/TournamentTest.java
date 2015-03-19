package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.TrumpsBasedStrategy;
import net.declension.games.cards.ohhell.strategy.SimpleRandomStrategy;
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

public class TournamentTest extends BaseGameTest {

    private static final int NUM_PLAYERS = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentTest.class);
    private static final int MAX_HAND_SIZE = 8;

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup(MAX_HAND_SIZE);
        players = new ArrayList<>();
        players.addAll(generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(NUM_PLAYERS, new SimpleRandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(NUM_PLAYERS, new TrumpsBasedStrategy(gameSetup), gameSetup));
    }

    @Test
    public void playLotsOfGamesShould() {
        Map<Player, Integer> stats = new Tournament(players, gameSetup).playMultipleGamesSequentially(10);
        List<String> sortedStats = stats.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Integer::compare))
                .map(entry -> format("%s for %s", entry.getValue(), entry.getKey()))
                .collect(toList());
        LOGGER.warn("Final rankings: {}", sortedStats);
    }
}