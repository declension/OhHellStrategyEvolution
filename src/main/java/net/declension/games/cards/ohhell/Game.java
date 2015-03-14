package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.ea.cards.ohhell.GameSetup;
import net.declension.ea.cards.ohhell.Player;
import net.declension.games.cards.*;
import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.sorting.TrumpsAwareSuitsFirstComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<Player> players;
    GameSetup setup;

    private Scorer scorer;
    private Predicate<SlotsMap<Player, Short>> bidValidator;
    private SlotsMap<Player, Set<Card>> cardsForPlayers;
    private Suit currentTrumps;

    public Game(List<Player> players) {
        this.players = players;
        LOGGER.info("Setting up {} players for this game: {}", players.size(), players);
        cardsForPlayers = new SlotsMap<>(players);
    }

    public void playRound(int handSize) {
        Deck deck = new Deck().shuffled();
        LOGGER.debug("Deck has {} cards before dealing: {}", deck.size(), deck);
        deal(handSize, deck, cardsForPlayers);
        LOGGER.debug("Deck now has {} cards.", deck.size());
        LOGGER.info("Players cards = {}.", cardsForPlayers);
    }

    private void deal(int number, Deck deck, SlotsMap<Player, Set<Card>> playersCards) {
        playersCards.keySet().stream()
                .forEach(player -> IntStream.rangeClosed(1, number)
                        .forEach(i -> {
                            Set<Card> playerCards = playersCards.get(player);
                            if (playerCards == null) {
                                playerCards = new TreeSet<>(new TrumpsAwareSuitsFirstComparator(currentTrumps, new AceHighRankComparator()));
                                playersCards.put(player, playerCards);
                            }
                            playerCards.add(deck.topCard());
                        }));

    }

    public SlotsMap<Player, Set<Card>> getCardsForPlayers() {
        return cardsForPlayers;
    }
}
