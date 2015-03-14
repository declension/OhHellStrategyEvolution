package net.declension.games.cards;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RankTest {

    @Test
    public void testAll() {
        assertThat(Rank.ALL_RANKS).hasSize(Rank.values().length);
    }
}