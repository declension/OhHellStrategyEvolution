package net.declension.ea.cards.ohhell.data;

import java.util.List;

public interface InGameEvaluationContext {

    /**
     * The starting number of cards for each player, this round.
     * @return a number between 1 and the maximum allowed by the rules.
     */
    Range handSize();

    /**
     * The rankings of the trumps.
     * @return a no-duplicate list of rankings in the range (1-13)
     */
    List<RankRanking> myTrumpsCardRanks();

    /**
     * The ranks of each of the non-trumps suits.
     * @return a 3 or 4 item list
     */
    List<List<RankRanking>> myOtherSuitsCardRanks();
}
