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

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

/**
 * The setup, rules, and scoring system for a Game.
 */
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

    public static List<Integer> standardOhHellHandSizeSequence(int maxHandSize) {
        List<Integer> ordered = rangeClosed(1, maxHandSize).boxed().collect(toList());
        List<Integer> full = new ArrayList<>(ordered);
        Collections.reverse(ordered);
        full.addAll(ordered.subList(1, maxHandSize));
        return full;
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

    /**
     * @return The Rank comparator, which should be fairly static. This allows different types of decks.
     */
    public Comparator<Rank> getRankComparator() {
        return ACE_HIGH_RANK_COMPARATOR;
    }

    /**
     * @return A stream of how many cards should be dealt.
     */
    public Stream<Integer> getRoundSizeSupplier() {
        return roundSizeSupplier.get();
    }

    /**
     * @return The scorer used to score each round for each player.
     */
    public Scorer getScorer() {
        return scorer;
    }

    /**
     * @return The rules to use.
     */
    public OhHellRules getRules() {
        return rules;
    }
}
