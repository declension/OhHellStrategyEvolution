package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Optional;
import java.util.Set;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public interface AverageBidStrategy extends BiddingStrategy {
    @Override
    default Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                              AllBids bidsSoFar,
                              Set<Integer> allowedBids) {
        double expectedTaken = (double) myCards.size() / bidsSoFar.capacity();
        return chooseLowestSquareUsingFunction(allowedBids, bid -> bid - expectedTaken);
    }

}
