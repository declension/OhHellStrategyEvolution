package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public class SimpleStrategy implements Strategy {

    public static final String NAME = "Simple";
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleStrategy.class);
    private final GameSetup gameSetup;

    public SimpleStrategy(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                             AllBids bidsSoFar, Set<Integer> allowedBids) {
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }

    static Integer closestToMean(Set<Integer> allowedBids, AllBids bidsSoFar, int handSize) {
        double expectedTaken = (double) handSize / bidsSoFar.getCapacity();
        Function<Integer, Double> distanceFromMean = bid -> bid - expectedTaken;
        return chooseLowestSquareUsingFunction(allowedBids, distanceFromMean);
    }

    @Override
    public Card chooseCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                           Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
        if (allowedCards.size() == 1) {
            return allowedCards.iterator().next();
        }
        if (trickSoFar.isEmpty()) {
            return chooseLeadingCard(trumps, me, myCards, tricksBid, tricksTaken, allowedCards);
        }
        return chooseFollowingCard(trumps, me, myCards, tricksBid, tricksTaken, trickSoFar, allowedCards);
    }

    protected Card chooseFollowingCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                                       Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                                       Trick trickSoFar,
                                       Set<Card> allowedCards) {
        Comparator<Card> cmp = gameSetup.createTrickScoringComparator(trumps, trickSoFar.leadingSuit());
        if (needToWin(me, tricksBid, tricksTaken)) {
            return highestAllowed(allowedCards, cmp);
        } else {
            Card currentWinningCard = trickSoFar.currentWinningCard().get();
            LOGGER.debug("{} is currently winning card", currentWinningCard);
            Optional<Card> highestLosing = allowedCards.stream()
                    .filter(c -> cmp.compare(c, currentWinningCard) == -1)
                    .sorted(cmp)
                    .findFirst();
            if (highestLosing.isPresent()) {
                LOGGER.debug("Gonna play the highest losing card: {}", highestLosing.get());
                return highestLosing.get();
            }
            return lowestAllowed(allowedCards, cmp);
        }
    }

    protected static boolean needToWin(Player me, Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken) {
        int myScore = tricksTaken.get(me);
        int delta = myScore - tricksBid.get(me);
        LOGGER.debug("Aiming {}: got {} point(s), so at {}{}",
                     delta < 0? "high": "low", myScore, delta >= 0? "+" : "", delta);
        return delta < 0;
    }

    protected Card chooseLeadingCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                                     Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                                     Set<Card> allowedCards) {

        Comparator<Card> cmp = gameSetup.createTrickScoringComparator(trumps, Optional.empty());
        if (needToWin(me, tricksBid, tricksTaken)) {
            return highestAllowed(allowedCards, cmp);
        } else {
            return lowestAllowed(allowedCards, cmp);
        }
    }

    private Card lowestAllowed(Set<Card> allowedCards, Comparator<Card> cmp) {
        return allowedCards.stream().sorted(cmp).findFirst().get();
    }

    private Card highestAllowed(Set<Card> allowedCards, Comparator<Card> cmp) {
        return allowedCards.stream().sorted(cmp.reversed()).findFirst().get();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return getName();
    }
}
