package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.strategy.SimpleStrategy;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TournamentTest extends BaseGameTest {

    private static final int NUM_PLAYERS = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentTest.class);

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup();
        players = generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup.getRNG()), gameSetup);
    }

    @Test
    public void playLotsOfGamesShould() {
        LOGGER.warn("Final rankings: {}", new Tournament(players, gameSetup).playLotsOfGames(100));
    }
}