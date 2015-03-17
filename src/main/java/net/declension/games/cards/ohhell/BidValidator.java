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

    default Set<Integer> getAllowedBidsForPlayer(Player player, Integer handSize, AllBids bidsSoFar) {
        return IntStream.rangeClosed(0, handSize).boxed()
                .filter(bid -> test(bidsSoFar.copyWithBid(player, bid)))
                .collect(toSet());
    }

}