package net.declension.ea.cards.ohhell;

import net.declension.games.cards.Rank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;

public class RankRanking extends Number implements Comparable<RankRanking> {
    private final int ranking;

    public RankRanking(Rank rank, Comparator<Rank> ordering) {
        List<Rank> values = asList(Rank.values());
        Collections.sort(values, ordering);
        ranking = 1 + values.indexOf(rank);
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
}
