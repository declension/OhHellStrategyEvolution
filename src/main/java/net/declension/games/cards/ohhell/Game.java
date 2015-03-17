package net.declension.games.cards.ohhell;

import com.google.common.collect.Lists;
import net.declension.collections.ImmutableCircularList;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.tricks.BidAndTaken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static java.lang.String.format;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<? extends Player> players;
    private Player dealer;
    private final GameSetup setup;

    private Suit trumps;
    private BidValidator bidValidator;
    private Map<? extends Player, BidAndTaken> bidAndTakens;


    public Game(List<? extends Player> players, GameSetup setup, Player dealer) {
        this.players = new ImmutableCircularList<>(players);
        this.setup = setup;
        this.dealer = dealer;
        LOGGER.info("Setting up {} players for this game: {}", players.size(), players);
    }

    public void play() {
        setup.getRoundsProducer().forEach(this::playRound);
    }

    void playRound(Integer handSize) {
        LOGGER.info("============== Player {} is dealing {} card(s) to each player ==============", dealer, handSize);
        Deck deck = new Deck().shuffled();
        deal(handSize, deck, players);
        trumps = deck.pullTopCard().suit();
        LOGGER.info("Trumps are {}", trumps);

        bidValidator = new BidBustingRulesBidValidator(handSize);
        AllBids bids = new AllBids(players);
        takeBids(handSize, bids);
        Player startingPlayer = getNextDealer();

        while (dealer.hasCards()) {
            startingPlayer = playTrickStartingWith(startingPlayer);
        }

        dealer = getNextDealer();
    }

    private Player getNextDealer() {
        return players.get(getNextDealerIndex());
    }

    private Player playTrickStartingWith(Player starter) {
        LOGGER.info("==== new trick led by {} ====", starter);
        Trick trickSoFar = new Trick(players, new SetTrickLeadSuitFirstCardListener());
        roundTheTableFrom(starter, player -> {
            Card card = player.playCard(this, trickSoFar);
            checkForNullCardFrom(player, card);
            trickSoFar.put(player, card);
            LOGGER.info("{} played {}", player, card);
        });
        Player winner= trickSoFar.winningPlayer();
        LOGGER.info("{} won that trick with {}.", winner, trickSoFar.get(winner));
        return winner;
    }

    private void roundTheTableAfterDealer(Consumer<Player> playerConsumer) {
        players.listIterator(getNextDealerIndex()).forEachRemaining(playerConsumer);
    }

    private void roundTheTableFrom(Player player, Consumer<Player> playerConsumer) {
        players.listIterator(players.indexOf(player)).forEachRemaining(playerConsumer);
    }


    private void checkForNullCardFrom(Player player, Card card) {
        if (card == null) {
            throw new IllegalStateException(
                    format("%s tried to play a null card (using %s)", player, player.getStrategy()));
        }
    }

    private void takeBids(Integer handSize, AllBids bids) {
        roundTheTableAfterDealer(player -> {
            Integer bid = player.bid(this, bids);
            bids.put(player, bid);
            doubleCheckBids(handSize, bids, player);
        });
        int total = bids.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.info("Here are the {} bids totalling {}: {}", bids.size(), total, bids);
    }

    private void doubleCheckBids(Integer handSize, AllBids bids, Player player) {
        if (!bidValidator.test(bids)) {
            throw new IllegalStateException(
                    format("Oh dear: %s had tried a dodgy bid of %d for a trick of size %d. The rest: %s",
                            player, bids.get(player), handSize, bids));
        }
    }

    private int getNextDealerIndex() {
        return players.indexOf(dealer) + 1;
    }

    private void deal(Integer number, Deck deck, Collection<? extends Player> players) {
        List<Card> dealtCards = deck.pullCards(players.size() * number);

        // It's fully random, so don't have to deal one card at a time - just give n cards to each player
        giveHandsToPlayers(players, Lists.partition(dealtCards, number));

        LOGGER.debug("After dealing: {}", deck);
    }

    /**
     * A would-be {@code zip} method.
     * @param players the players
     * @param hands the list of hands to be given out, <strong>in the same order</strong>.
     */
    private void giveHandsToPlayers(Collection<? extends Player> players, List<List<Card>> hands) {
        Iterator<? extends Player> itr = players.iterator();
        for (int i =0; i < players.size(); i++) {
            itr.next().receiveNewHand(trumps, hands.get(i));
        }
    }

    public Suit getTrumps() {
        return trumps;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Game{");
        sb.append("players=").append(players);
        sb.append(", dealer=").append(dealer);
        sb.append(", setup=").append(setup);
        sb.append(", trumps=").append(trumps);
        sb.append(", bidValidator=").append(bidValidator);
        sb.append(", bidAndTakens=").append(bidAndTakens);
        sb.append('}');
        return sb.toString();
    }

    public BidValidator getBidValidator() {
        return bidValidator;
    }

    public Map<? extends Player, BidAndTaken> getBidAndTakens() {
        return bidAndTakens;
    }

    private class SetTrickLeadSuitFirstCardListener implements FirstCardListener<Trick> {
        @Override
        public void onFirstCard(Trick trick, Card firstCard) {
            LOGGER.info("Leading suit is {}, trumps are {}", firstCard.suit(), trumps);
            trick.setCardOrdering(setup.createTrickComparator(getTrumps(), firstCard.suit()));
        }
    }
}
