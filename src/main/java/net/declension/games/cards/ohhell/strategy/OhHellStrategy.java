package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.ohhell.strategy.bidding.BiddingStrategy;
import net.declension.games.cards.ohhell.strategy.playing.PlayingStrategy;

/**
 * Interface that all overall strategies must implement.
 * However, they may choose to compose individual strategy elements to complete this.
 */
public interface OhHellStrategy extends BiddingStrategy, PlayingStrategy {
    default String getName() {
        return getClass().getSimpleName();
    }

}
