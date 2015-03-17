package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.*;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class GameSetup {
    public static final AceHighRankComparator ACE_HIGH_RANK_COMPARATOR = new AceHighRankComparator();
    private final Random random = new MersenneTwisterRNG();
    private Stream<Integer> roundsProducer;

    public GameSetup(Stream<Integer> roundsProducer) {
        this.roundsProducer = roundsProducer;
    }

    public Random getRNG() {
        return random;
    }

    public Comparator<Card> createRoundComparator(Suit trumps) {
        return new SuitThenRankComparator(getRankComparator(), new TrumpsFirstSuitComparator(trumps));
    }

    public Comparator<Card> createTrickComparator(Suit trumps, Suit lead) {
        return new SuitThenRankComparator(getRankComparator(), new TrumpsThenLeadSuitComparator(trumps, lead));
    }

    public Comparator<Card> createGeneralComparator(Suit trumps) {
        return new TrumpsSuitThenRankCardComparator(getRankComparator(), trumps);
    }

    public Comparator<Rank> getRankComparator() {
        return ACE_HIGH_RANK_COMPARATOR;
    }

    public Stream<Integer> getRoundsProducer() {
        return roundsProducer;
    }
}
