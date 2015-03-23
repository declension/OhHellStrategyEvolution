package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;

import java.util.Optional;

import static net.declension.utils.Validation.requireNonNullParam;
import static net.declension.games.cards.sorting.Comparators.equalityComparator;

/**
 * Trumps is the most important, then lead suit, then everything else in enum order.
 */
public class TrumpsThenLeadSuitComparator extends BaseSuitComparator {

    public TrumpsThenLeadSuitComparator(Optional<Suit> trumps, Optional<Suit> lead) {
        requireNonNullParam(trumps, "Trumps must be specified (as Optional)");
        requireNonNullParam(lead, "Lead must be specified (as Optional)");
        trumps.ifPresent(t -> safelyAppend(equalityComparator(t)));
        lead.ifPresent(l -> safelyAppend(equalityComparator(l)));
        //safelyAppend(new EnumComparator<>());
    }
}
