package net.declension.ea.cards.ohhell;

import java.util.*;

/**
 * A value-add list wrapper, providing interesting statistics on the underlying data.
 * @param <T>
 */
public class StatsList<T extends Number> {
    public static final double DEFAULT_MEAN = 0.0;
    private final List<T> data;
    private final EnumMap<StatsType, Number> stats;

    public StatsList(List<? extends T> input, Comparator<T> ordering) {
        data = new ArrayList<>(input);
        Collections.sort(data, ordering);
        stats = buildStats();
    }

    /**
     * Could use {@link java.util.DoubleSummaryStatistics}, but it doesn't seem feature-rich enough to warrant this.
     * @return a map of stats.
     */
    private EnumMap<StatsType, Number> buildStats() {
        EnumMap<StatsType, Number> ret = new EnumMap<>(StatsType.class);
        ret.put(StatsType.COUNT, data.size());
        ret.put(StatsType.MIN, data.get(0));
        ret.put(StatsType.MAX, data.get(data.size() - 1));
        ret.put(StatsType.MEAN, getMean());
        ret.put(StatsType.VARIANCE, getVariance());
        return ret;
    }

    public Number getStats(StatsType statsType) {
        return stats.get(statsType);
    }

    private Number getMean() {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(DEFAULT_MEAN);
    }

    public Double getVariance() {
        return data.stream()
                   .mapToDouble(Number::doubleValue)
                   .reduce(0, (l, r) -> l + r * r);
    }

    public enum StatsType {
        COUNT,
        MIN,
        MAX,
        MEAN,
        VARIANCE,
        ;
    }
}
