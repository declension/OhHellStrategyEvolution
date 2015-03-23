package net.declension.games.cards.ohhell.strategy.playing;

import net.declension.collections.CollectionUtils;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SimplePlayingStrategy extends PlayingStrategy {
    final static Logger LOGGER = LoggerFactory.getLogger(SimplePlayingStrategy.class);

    GameSetup getGameSetup();

    @Override
    default Card chooseCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                           Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
        LOGGER.debug("Hmm, here's my hand: {}", myCards);
        if (trickSoFar.isEmpty()) {
            return chooseLeadingCard(trumps, me, myCards, tricksBid, tricksTaken, allowedCards);
        }
        return chooseFollowingCard(trumps, me, myCards, tricksBid, tricksTaken, trickSoFar, allowedCards);
    }

    default Card chooseFollowingCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                                       Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                                       Trick trickSoFar,
                                       Set<Card> allowedCards) {
        Comparator<Card> cmp = getGameSetup().createTrickScoringComparator(trumps, trickSoFar.leadingSuit());
        Card currentWinningCard = trickSoFar.currentWinningCard().get();
        LOGGER.debug("{} is the currently winning card", currentWinningCard);
        if (needToWin(me, tricksBid, tricksTaken)) {
            if (trickSoFar.remaining() == 1) {
                Optional<Card> lowestWinning = CollectionUtils.lowestAbove(allowedCards, cmp, currentWinningCard);
                if (lowestWinning.isPresent()) {
                    LOGGER.debug("I'm last, so I'll play the lowest winning card: {}", lowestWinning.get());
                    return lowestWinning.get();
                }
            }
            return highest(allowedCards, cmp);
        } else {
            Optional<Card> highestLosing = CollectionUtils.highestBelow(allowedCards, cmp, currentWinningCard);
            if (highestLosing.isPresent()) {
                LOGGER.debug("Gonna play the highest losing card: {}", highestLosing.get());
                return highestLosing.get();
            }
            return lowest(allowedCards, cmp);
        }
    }

    static boolean needToWin(Player me, Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken) {
        int myScore = tricksTaken.get(me);
        int delta = myScore - tricksBid.get(me);
        LOGGER.debug("Aiming {}: got {} point(s), so at {}{}",
                     delta < 0? "high": "low", myScore, delta >= 0? "+" : "", delta);
        return delta < 0;
    }

    default Card chooseLeadingCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                                     Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                                     Set<Card> allowedCards) {

        Comparator<Card> cmp = getGameSetup().createTrickScoringComparator(trumps, Optional.empty());
        if (needToWin(me, tricksBid, tricksTaken)) {
            return highest(allowedCards, cmp);
        } else {
            return lowest(allowedCards, cmp);
        }
    }

    static Card lowest(Set<Card> cards, Comparator<Card> cmp) {
        return cards.stream().sorted(cmp).findFirst().get();
    }

    static Card highest(Set<Card> cards, Comparator<Card> cmp) {
        return cards.stream().sorted(cmp.reversed()).findFirst().get();
    }

}
