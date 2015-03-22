package net.declension.games.cards.sorting.suit;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.Suit;

import java.util.Optional;

/**
 * Orders by the "traditional" ordering as defined in {@link Suit} enum,
 * but with the Trumps ordered highest. Useful for display a hand of cards.
 */
public class TrumpsHighDisplaySuitComparator extends BaseSuitComparator {


    public TrumpsHighDisplaySuitComparator(Optional<Suit> trumps) {
        safelyAppend(new TrumpsOrNothingSuitComparator(trumps));
        safelyAppend(new EnumComparator<>());
    }

    @Override
    public int compare(Suit o1, Suit o2) {
        return comparator.compare(o1, o2);
    }
}
