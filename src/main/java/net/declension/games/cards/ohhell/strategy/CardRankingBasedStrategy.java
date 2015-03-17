package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CardRankingBasedStrategy extends SimpleStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardRankingBasedStrategy.class);
    private final GameSetup gameSetup;
    private final List<Card> allCards;

    public CardRankingBasedStrategy(Random rng, GameSetup gameSetup) {
        super(rng, gameSetup);
        this.gameSetup = gameSetup;
        allCards = Card.allPossibleCards();
    }

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards, AllBids bidsSoFar, Set<Integer> allowedBids) {
        int handSize = myCards.size();
        double numTrumps = myCards.stream().filter(card -> card.suit() == trumps).count();
        double trumpsDelta = (numTrumps - handSize * 12.0 / 51.0) / handSize;
        LOGGER.info("Trumps delta for {} is {}", myCards, Math.pow(2, trumpsDelta) - 0.5);
        Collections.sort(allCards, gameSetup.createGeneralComparator(trumps));
        LOGGER.info("Cards in order for trumps {}: {} ", trumps, allCards);
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }
}
