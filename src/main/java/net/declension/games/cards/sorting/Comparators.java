package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import net.declension.games.cards.sorting.suit.TrumpsThenLeadSuitComparator;

import java.util.Comparator;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static net.declension.utils.Validation.requireNonNullParam;

public class Comparators {

    public static Comparator<Card> rankOnlyComparator(Comparator<Rank> rankComparator) {
        return comparing(Card::rank, rankComparator);
    }

    public static Comparator<Card> standardComparator(Optional<Suit> trumps, Optional<Suit> lead) {
        return comparing(Card::suit, new TrumpsThenLeadSuitComparator(trumps, lead))
                       .thenComparing(rankOnlyComparator(new AceHighRankComparator()));
    }

    /**
     * Partitioning comparator: only cares if items are equal to the given item or not. Everything else is equal.
     * @param value    The value to be equal to if you want to be high up in the ranking.
     * @param <T>      Type of the values.
     * @return a comparator
     */
    public static <T> Comparator<T> equalityComparator(T value) {
        requireNonNullParam(value, "value");
        return (l, r) -> Boolean.compare(value.equals(l), value.equals(r));
    }
}
