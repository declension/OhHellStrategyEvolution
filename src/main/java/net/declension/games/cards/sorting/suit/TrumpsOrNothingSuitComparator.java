package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;

import java.util.Optional;

import static net.declension.games.cards.sorting.Comparators.equalityComparator;

/**
 * All suits are equal, except Trumps which is highest.
 */
public class TrumpsOrNothingSuitComparator extends BaseSuitComparator {

    public TrumpsOrNothingSuitComparator(Optional<Suit> trumps) {
        if (trumps.isPresent()) {
            safelyAppend(equalityComparator(trumps.get()));
        } else {
            comparator = NO_OP_COMPARATOR;
        }
    }
}
