package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.scoring.RikikiScorer;
import net.declension.games.cards.ohhell.scoring.Scorer;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.TrumpsThenLeadScoringComparator;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import net.declension.games.cards.sorting.suit.TrumpsHighDisplaySuitComparator;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GameSetup {
    public static final AceHighRankComparator ACE_HIGH_RANK_COMPARATOR = new AceHighRankComparator();
    private final Random random = new MersenneTwisterRNG();
    private final Supplier<Stream<Integer>> roundSizeSupplier;
    private final Scorer scorer;
    private final OhHellRules rules;

    public GameSetup(Supplier<Stream<Integer>> roundSizeSupplier, OhHellRules rules) {
        this.roundSizeSupplier = roundSizeSupplier;
        this.rules = rules;
        scorer = new RikikiScorer();
    }

    public Random getRNG() {
        return random;
    }

    /**
     * A comparator for scoring cards in a trick.
     * with the best suits (lead then trump) last.
     *
     * @param trumps the current trumps
     * @param lead an Optional current lead suit.
     * @return a Comparator for cards.
     */
    public Comparator<Card> createTrickScoringComparator(Optional<Suit> trumps, Optional<Suit> lead) {
        return new TrumpsThenLeadScoringComparator(new AceHighRankComparator(), trumps, lead);
    }

    /**
     * A comparator suitable for display cards nicely, i.e. arranged in suits from high to low,
     * with the best suits (lead then trump) last.
     *
     * @param trumps the current trumps
     * @return a Comparator for cards.
     */
    public Comparator<Card> createDisplayComparator(Optional<Suit> trumps) {
        return new SuitThenRankComparator(new TrumpsHighDisplaySuitComparator(trumps), getRankComparator());
    }

    public Comparator<Rank> getRankComparator() {
        return ACE_HIGH_RANK_COMPARATOR;
    }

    public Stream<Integer> getRoundSizeSupplier() {
        return roundSizeSupplier.get();
    }

    public Scorer getScorer() {
        return scorer;
    }

    public OhHellRules getRules() {
        return rules;
    }
}
