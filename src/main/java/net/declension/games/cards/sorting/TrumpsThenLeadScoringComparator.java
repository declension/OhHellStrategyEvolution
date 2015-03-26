package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.suit.TrumpsThenLeadThenEqualSuitComparator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static net.declension.games.cards.Suit.ALL_SUITS;
import static net.declension.utils.Validation.requireNonNullParam;

public class TrumpsThenLeadScoringComparator implements Comparator<Card> {
    private final Comparator<Rank> rankComparator;
    private final Comparator<Suit> suitComparator;
    private final Set<Suit> equalSuits = new HashSet<>(ALL_SUITS);

    public TrumpsThenLeadScoringComparator(Comparator<Rank> rankComparator, Optional<Suit> trumps,
                                           Optional<Suit> lead ) {
        requireNonNullParam(rankComparator, "Rank Comparator");
        this.rankComparator = rankComparator;
        requireNonNullParam(trumps, "Trumps must be specified (as Optional)");
        requireNonNullParam(lead, "Lead must be specified (as Optional)");
        suitComparator = new TrumpsThenLeadThenEqualSuitComparator(trumps, lead);
        trumps.ifPresent(equalSuits::remove);
        lead.ifPresent(equalSuits::remove);
    }

    @Override
    public int compare(Card left, Card right) {
        int suitComparison = suitComparator.compare(left.suit(), right.suit());
        if (suitComparison != 0) {
            return suitComparison;
        }
        // Both suits are the same now.
        Suit suit = left.suit();
        if (equalSuits.contains(suit)) {
            // If you're not trumps or lead, you're a trash card.
            return 0;
        }
        return rankComparator.compare(left.rank(), right.rank());
    }

}
