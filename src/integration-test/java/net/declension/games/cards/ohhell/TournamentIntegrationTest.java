package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.AverageBidRandomStrategy;
import net.declension.games.cards.ohhell.strategy.RandomStrategy;
import net.declension.games.cards.ohhell.strategy.SimpleStrategy;
import net.declension.games.cards.ohhell.strategy.TrumpsBasedRandomStrategy;
import org.assertj.core.api.Condition;
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
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;

public class TournamentIntegrationTest extends BaseGameTest {

    private static final int NUM_PLAYERS = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentIntegrationTest.class);
    private static final int MAX_HAND_SIZE = 51 / (4 * NUM_PLAYERS);
    public static final int NUM_GAMES = 40;
    private List<Player> simples;

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup(MAX_HAND_SIZE);
        players = new ArrayList<>();
        players.addAll(generatePlayers(NUM_PLAYERS, new AverageBidRandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(1, new RandomStrategy(gameSetup.getRNG()), gameSetup));
        players.addAll(generatePlayers(NUM_PLAYERS, new TrumpsBasedRandomStrategy(gameSetup.getRNG()), gameSetup));
        simples = generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup), gameSetup);
        players.addAll(simples);
    }

    @Test
    public void playLotsOfGamesShouldMeanSimpleStrategyWins() {
        Map<Player, Double> stats = new Tournament(players, gameSetup).playMultipleGamesSequentially(NUM_GAMES);
        List<Map.Entry<Player, Double>> ranked = stats.entrySet().stream()
                                                     .sorted(comparing(Map.Entry::getValue, Double::compare))
                                                     .collect(toList());
        List<String> sortedStats = ranked.stream()
                                        .map(entry -> format("%.2f for %s", entry.getValue(), entry.getKey()))
                                        .collect(toList());
        LOGGER.warn("Average rankings after {} games: {}", NUM_GAMES, sortedStats);


        // Horrible
        ArrayList<Player> orderedPlayers = new ArrayList<>();
        orderedPlayers.addAll(stats.keySet());
        Condition<Integer> winning = new Condition<Integer>() {
            @Override
            public boolean matches(Integer value) {
                return value <= simples.size();
            }
        };
        assertThat(simples).extracting(ranked::indexOf).are(allOf(winning));
    }
}