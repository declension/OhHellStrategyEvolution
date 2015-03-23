package net.declension.games.cards.ohhell.strategy.playing;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static net.declension.collections.CollectionUtils.pickRandomly;

public interface RandomPlayingStrategy extends PlayingStrategy {

    Random getRng();

    @Override
    default Card chooseCard(Optional<Suit> trumps, Player me, Set<Card> myCards,
                           Map<Player, Integer> tricksBids,
                           Map<Player, Integer> tricksTaken,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
        return pickRandomly(getRng(), allowedCards);
    }
}
