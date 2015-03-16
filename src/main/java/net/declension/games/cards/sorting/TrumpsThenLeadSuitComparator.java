package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;

import java.util.Comparator;

public class TrumpsThenLeadSuitComparator implements Comparator<Suit> {
    private final Suit trumps;
    private final Suit lead;

    public TrumpsThenLeadSuitComparator(Suit trumps, Suit lead) {
        this.trumps = trumps;
        this.lead = lead;
    }

    @Override
    public int compare(Suit o1, Suit o2) {
        int trumpDiff = trumps == null? 0 : Boolean.compare(trumps.equals(o1), trumps.equals(o2));
        if (trumpDiff == 0) {
            int leadDiff = lead == null? 0 : Boolean.compare(lead.equals(o1), lead.equals(o2));
            return leadDiff == 0? Integer.compare(o1.ordinal(), o2.ordinal()) : leadDiff;
        } else {
            return trumpDiff;
        }
    }
}
