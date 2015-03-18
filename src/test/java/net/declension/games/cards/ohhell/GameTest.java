package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.strategy.SimpleStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class GameTest extends BaseGameTest {

    public static final int NUM_PLAYERS = 4;
    public static final int HAND_SIZE = 8;

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup();
        players = generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup.getRNG()), gameSetup);
        game = new Game(players, gameSetup, players.get(0));
    }

    @Test
    public void playShouldWork() {
        game.play();
        assertThatNobodyHasCards();
    }

    @Test
    public void testPlayRound() {
        game.playRound(HAND_SIZE);
        assertThatNobodyHasCards();
        assertThat(totalScores()).isEqualTo(HAND_SIZE);
    }

}