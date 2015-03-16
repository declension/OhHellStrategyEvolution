package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;
import org.junit.Test;

import java.util.Comparator;

import static net.declension.games.cards.Suit.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsThenLeadSuitComparatorTest {

    private Comparator<Suit> cmp;

    @Test
    public void compareShouldRespectEnum() {
        cmp = new TrumpsThenLeadSuitComparator(SPADES, null);
        assertThat(CLUBS).usingComparator(cmp)
                .isLessThan(DIAMONDS)
                .isLessThan(HEARTS);
        assertThat(SPADES).usingComparator(cmp).isEqualTo(SPADES);
        assertThat(CLUBS).usingComparator(cmp).isEqualTo(CLUBS);
    }

    @Test
    public void trumpsShouldTrump() {
        cmp = new TrumpsThenLeadSuitComparator(SPADES, null);
        assertThat(CLUBS).usingComparator(cmp).isLessThan(SPADES);
        assertThat(HEARTS).usingComparator(cmp).isLessThan(SPADES);
        assertThat(DIAMONDS).usingComparator(cmp).isLessThan(SPADES);
        assertThat(SPADES).usingComparator(cmp).isEqualTo(SPADES);
    }

    @Test
    public void leadsShouldBeatNormal() {
        cmp = new TrumpsThenLeadSuitComparator(null, DIAMONDS);
        assertThat(CLUBS).usingComparator(cmp).isLessThan(DIAMONDS);
        assertThat(HEARTS).usingComparator(cmp).isLessThan(DIAMONDS);
        assertThat(SPADES).usingComparator(cmp).isLessThan(DIAMONDS);
        assertThat(DIAMONDS).usingComparator(cmp).isEqualTo(DIAMONDS);
    }

    @Test
    public void trumpsShouldBeatLeads() {
        cmp = new TrumpsThenLeadSuitComparator(CLUBS, DIAMONDS);
        assertThat(DIAMONDS).usingComparator(cmp).isLessThan(CLUBS);
        assertThat(DIAMONDS).usingComparator(cmp).isEqualTo(DIAMONDS);
    }

}