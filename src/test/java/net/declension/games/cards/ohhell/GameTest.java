package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.strategy.AverageStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class GameTest extends BaseGameTest {

    public static final int NUM_PLAYERS = 4;
    public static final int HAND_SIZE = 8;

    @Before
    public void setUp() {
        gameSetup = createDefaultGameSetup(HAND_SIZE);
        players = generatePlayers(NUM_PLAYERS, new AverageStrategy(gameSetup.getRNG()), gameSetup);
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