package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;

import java.util.List;
import java.util.Random;

public class CardRankRandomStrategy extends RandomStrategy {
    static final String NAME = "RND|RND";

    @Override
    public String getName() {
        return NAME;
    }

    private final List<Card> allCards;

    public CardRankRandomStrategy(Random rng) {
        super(rng);
        allCards = Card.allPossibleCards();
    }

    //Collections.sort(allCards, gameSetup.createGeneralComparator(trumps));
    //LOGGER.debug("Cards in order for trumps {}: {} ", trumps, allCards);
}
