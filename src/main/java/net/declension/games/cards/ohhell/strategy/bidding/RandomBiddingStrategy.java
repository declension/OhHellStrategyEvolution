package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static net.declension.collections.CollectionUtils.pickRandomly;

public interface RandomBiddingStrategy extends BiddingStrategy {

    Random getRng();

    @Override
    default Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                             AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        return pickRandomly(getRng(), allowedBids);
    }
}
