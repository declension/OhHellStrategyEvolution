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
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The setup, rules, and scoring system for a Game, or set of Games.
 */
public class GameSetup {
    public static final AceHighRankComparator ACE_HIGH_RANK_COMPARATOR = new AceHighRankComparator();
    public static final int MAX_PLAYERS = 10;

    private final Random random = new MersenneTwisterRNG();
    private final RoundSizer roundSizer;
    private final Scorer scorer;
    private final OhHellRules rules;

    public GameSetup(RoundSizer roundSizer, OhHellRules rules) {
        this.roundSizer = roundSizer;
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

    /**
     * @return The Rank comparator, which should be fairly static. This allows different types of decks.
     */
    public Comparator<Rank> getRankComparator() {
        return ACE_HIGH_RANK_COMPARATOR;
    }

    /**
     * @return A list of how many cards should be dealt, for the entire game
     * @param numPlayers the number of players in the game
     */
    public List<Integer> allRoundSizesFor(int numPlayers) {
        return roundSizer.getFor(numPlayers);
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
