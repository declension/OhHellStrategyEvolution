package net.declension.games.cards;

import org.junit.Test;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

public class RankTest {

    @Test
    public void testAll() {
        assertThat(Rank.ALL_RANKS, iterableWithSize(Rank.values().length));
    }
}