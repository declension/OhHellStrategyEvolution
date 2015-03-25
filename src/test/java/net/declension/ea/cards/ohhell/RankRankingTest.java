package net.declension.ea.cards.ohhell;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.Rank;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RankRankingTest {

    public static final EnumComparator<Rank> ENUM_COMPARATOR = new EnumComparator<>();

    @Test
    public void intValueShould() {
        assertThat(new RankRanking(Rank.TWO, ENUM_COMPARATOR).intValue()).isEqualTo(1);
        assertThat(new RankRanking(Rank.THREE, ENUM_COMPARATOR).intValue()).isEqualTo(2);
        assertThat(new RankRanking(Rank.ACE, ENUM_COMPARATOR).intValue()).isEqualTo(13);

        assertThat(new RankRanking(Rank.ACE, ENUM_COMPARATOR.reversed()).intValue()).isEqualTo(1);
    }
}