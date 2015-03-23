package net.declension.games.cards.ohhell.player;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerIDTest {

    public static final String TEST_ID = "TEST";
    public static final PlayerID TEST_PLAYER_ID = new PlayerID(TEST_ID);
    public static final String ANOTHER_ID = "ANOTHER";
    public static final PlayerID OTHER_PLAYER_ID = new PlayerID(ANOTHER_ID);

    @Test
    public void equalsHappyPath() {
        assertThat(TEST_PLAYER_ID).isNotEqualTo(OTHER_PLAYER_ID);
        assertThat(TEST_PLAYER_ID).isEqualTo(TEST_PLAYER_ID);
        assertThat(TEST_PLAYER_ID).isEqualTo(new PlayerID(TEST_ID));
    }

    @Test
    public void equalsEdgeCases() {
        assertThat(TEST_PLAYER_ID).isNotEqualTo(null);
        assertThat(TEST_PLAYER_ID).isNotEqualTo("foo");
    }

    @Test
    public void hashCodeShouldWork() {
        assertThat(TEST_PLAYER_ID.hashCode()).isEqualTo(new PlayerID(TEST_ID).hashCode());
    }
}