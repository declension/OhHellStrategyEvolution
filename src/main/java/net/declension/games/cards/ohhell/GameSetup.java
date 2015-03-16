package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.TrumpsFirstSuitComparator;
import net.declension.games.cards.sorting.TrumpsThenLeadSuitComparator;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class GameSetup {
    private final Random random = new MersenneTwisterRNG();
    private Stream<Integer> roundsProducer;

    public Random getRNG() {
        return random;
    }

    public Function<Suit, Comparator<Card>> standardComparatorSupplier() {
        return trumps -> new SuitThenRankComparator(getRankComparator(), new TrumpsFirstSuitComparator(trumps));
    }

    public Comparator<Card> createTrickComparator(Suit trumps, Suit lead) {
        return new SuitThenRankComparator(getRankComparator(), new TrumpsThenLeadSuitComparator(trumps, lead));
    }

    public Comparator<Rank> getRankComparator() {
        return new AceHighRankComparator();
    }

    public Stream<Integer> getRoundsProducer() {
        return roundsProducer;
    }
}
