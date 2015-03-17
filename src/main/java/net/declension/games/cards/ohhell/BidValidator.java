package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

/**
 * The Validator should be able to check both finished and unfinished inputs.
 * {@code} null value means an unfinished bid.
 */
@FunctionalInterface
public interface BidValidator extends Predicate<AllBids> {

    /**
     * Slow but guaranteed way of finding all the valid bids for a player in the middle of a bidding round
     * modelled by {@code bidsSoFar}
     *
     * @param player the current player
     * @param handSize the number of cards in the current round
     * @param bidsSoFar A map of the finished (positive integer) and unfinished ({@code null}) bidding.
     *
     * @return a Set of the allowed, meaningful bids.
     */
    default Set<Integer> getAllowedBidsForPlayer(Player player, Integer handSize, AllBids bidsSoFar) {
        return IntStream.rangeClosed(0, handSize).boxed()
                .filter(bid -> test(bidsSoFar.copyWithBid(player, bid)))
                .collect(toSet());
    }

}
