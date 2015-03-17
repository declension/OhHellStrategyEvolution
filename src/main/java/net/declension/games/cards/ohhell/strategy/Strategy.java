package net.declension.games.cards.ohhell.strategy;

/**
 * Interface that all overall strategies must implement.
 * However, they may choose to compose individual strategy elements to complete this.
 */
public interface Strategy extends BiddingStrategy, PlayingStrategy {


}
