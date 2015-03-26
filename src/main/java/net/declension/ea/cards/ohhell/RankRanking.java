package net.declension.ea.cards.ohhell;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.Rank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static net.declension.collections.EnumComparator.Order.NATURAL;

public class RankRanking extends Number implements Comparable<RankRanking> {
    private final int ranking;
    private final Rank rank;

    public RankRanking(Rank rank, Comparator<Rank> ordering) {
        this.rank = rank;
        List<Rank> values = asList(Rank.values());
        Collections.sort(values, ordering);
        ranking = 1 + values.indexOf(rank);
    }

    public static RankRanking rankingOf(Rank rank) {
        return new RankRanking(rank, new EnumComparator<>(NATURAL));
    }

    @Override
    public int intValue() {
        return ranking;
    }

    @Override
    public long longValue() {
        return ranking;
    }

    @Override
    public float floatValue() {
        return ranking;
    }

    @Override
    public double doubleValue() {
        return ranking;
    }

    @Override
    public int compareTo(RankRanking other) {
        return Integer.compare(ranking, other.ranking);
    }

    @Override
    public String toString() {
        return "#" + ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Number)) {
            return false;
        }

        Number that = (Number) o;
        return ranking == that.intValue();
    }

    @Override
    public int hashCode() {
        return ranking;
    }
}
