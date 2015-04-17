package net.declension.games.cards.ohhell;

import net.declension.ea.cards.ohhell.BaseIntegrationTest;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class TournamentIntegrationTest extends BaseIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentIntegrationTest.class);

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup();
        players = new ArrayList<>();
        players.addAll(generatePlayers(1, new AverageRandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(1, new RandomRandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(1, new TrumpsBasedRandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(2, new TrumpsBasedSimpleStrategy(gameSetup), gameSetup));
        players.addAll(generatePlayers(2, new AverageSimpleStrategy(gameSetup), gameSetup));
    }

    @Test
    public void playLotsOfGamesShouldMeanSimpleStrategyWins() {
        Map<Player, Double> stats = new Tournament(players, gameSetup).playMultipleGamesSequentially
                (GAMES_PER_TOURNAMENT);
        List<Map.Entry<Player, Double>> ranked = stats.entrySet().stream()
                                                     .sorted(comparing(Map.Entry::getValue, Double::compare))
                                                     .collect(toList());
        List<String> sortedStats = ranked.stream()
                                        .map(entry -> format("%.2f for %s", entry.getValue(), entry.getKey()))
                                        .collect(toList());
        LOGGER.warn("Average rankings after {} games: {}", GAMES_PER_TOURNAMENT, sortedStats);


        // Horrible
        ArrayList<Player> orderedPlayers = new ArrayList<>();
        orderedPlayers.addAll(stats.keySet());
    }
}