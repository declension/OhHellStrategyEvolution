package net.declension.games.cards.ohhell.scoring;

public class RikikiScorer implements Scorer {
    public static final int SUCCESS_BONUS = 10;
    private static final Integer SUCCESS_MULTIPLIER = 2;
    private static final Integer FAILURE_MULTIPLIER = -2;
    private static final Integer FAILURE_PENALTY = 0;

    @Override
    public Integer scoreFor(Integer tricksBid, Integer tricksScored) {
        int delta = tricksScored - tricksBid;
        return delta == 0 ? SUCCESS_BONUS + tricksBid * SUCCESS_MULTIPLIER
                          : FAILURE_PENALTY + delta * FAILURE_MULTIPLIER;
    }
}
