package net.declension.games.cards.ohhell;

import net.declension.ea.cards.ohhell.GameSetup;
import net.declension.ea.cards.ohhell.Player;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.utils.SlotsMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Game {
    private static final int NUM_PLAYERS = 4;
    private final List<Player> players;
    GameSetup setup;

    //Predicate<SlotsMap<Short>, > validBid;
    private SlotsMap<Player, Set<Card>> cardsForPlayers;

    public Game(List<Player> players) {
        this.players = players;
        System.out.println(players);
        cardsForPlayers = new SlotsMap<>(players);
    }

    public void playRound(int handSize) {
        Deck deck = new Deck().shuffled();
        System.out.printf("Deck has %d cards\n", deck.size());
        deal(handSize, deck, cardsForPlayers);
        System.out.printf("Deck now has %d cards\n", deck.size());
        System.out.printf("Players cards = %s\n", cardsForPlayers);
    }

    private void deal(int number, Deck deck, SlotsMap<Player, Set<Card>> playersCards) {
        playersCards.keySet().stream()
                .forEach(player -> IntStream.rangeClosed(1, number)
                        .forEach(i -> {
                            Set<Card> playerCards = playersCards.get(player);
                            if (playerCards == null) {
                                playerCards = new HashSet<>();
                                playersCards.put(player, playerCards);
                            }
                            playerCards.add(deck.topCard());
                        }));

    }

    public SlotsMap<Player, Set<Card>> getCardsForPlayers() {
        return cardsForPlayers;
    }
}
