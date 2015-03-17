package net.declension.games.cards.ohhell.scoring;

@FunctionalInterface
public interface Scorer  {

    Integer scoreFor(Integer tricksBid, Integer tricksTaken);
}
