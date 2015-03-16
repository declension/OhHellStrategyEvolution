package net.declension.games.cards.ohhell;

import com.google.common.collect.Lists;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.cycle;
import static java.lang.String.format;
import static java.util.stream.IntStream.rangeClosed;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<Player> players;
    private PlayerID dealer;
    private final GameSetup setup;

    private Suit trumps;
    private BidValidator bidValidator;
    private Map<PlayerID, BidAndTaken> bidAndTakens;


    public Game(List<Player> players, GameSetup setup, PlayerID dealer) {
        this.players = players;
        this.setup = setup;
        this.dealer = dealer;
        LOGGER.info("Setting up {} players for this game: {}", players.size(), players);
    }

    public void play() {
        setup.getRoundsProducer().forEach(this::playRound);
    }

    void playRound(Integer handSize) {
        LOGGER.info("Dealer is {}", dealer);
        Deck deck = new Deck().shuffled();
        deal(handSize, deck, players);
        trumps = deck.pullTopCard().suit();
        LOGGER.info("Trumps are {}", trumps);

        bidValidator = new BidBustingRulesBidValidator(handSize);
        AllBids bids = AllBids.forPlayers(players);

        takeBids(handSize, bids);

        // Start with the left of the dealer;
        Iterator<Player> playersIterator = cycle(players).iterator();
        advanceIteratorToDealer(playersIterator);

        Trick trickSoFar = Trick.forPlayers(players);
        trickSoFar.addFirstCardListener(new SetTrickLeadSuitFirstCardListener());
        rangeClosed(1, handSize).boxed().forEach(i -> {
            Player player = playersIterator.next();
            Card card = player.playCard(this, trickSoFar);
            checkForNullCardFrom(player, card);
            trickSoFar.put(player.getID(), card);
            LOGGER.info("{} played {}", player, card);
        });
        PlayerID winner = trickSoFar.winningPlayer();
        LOGGER.info("{} won that trick with {}.", winner, trickSoFar.get(winner));
        dealer = advanceIteratorToDealer(playersIterator).next().getID();
    }

    private Iterator<Player> advanceIteratorToDealer(Iterator<Player> playersIterator) {
        while (playersIterator.next().getID() != dealer);
        return playersIterator;
    }

    private void checkForNullCardFrom(Player player, Card card) {
        if (card == null) {
            throw new IllegalStateException(
                    format("%s tried to play a null card (using %s)", player, player.getStrategy()));
        }
    }

    private void takeBids(Integer handSize, AllBids bids) {
        players.stream().forEach(player -> {
            Integer bid = player.bid(this, bids);
            bids.put(player.getID(), bid);
            // Bid is "guaranteed" valid
            if (!bidValidator.test(bids)) {
                throw new IllegalStateException(
                        format("Oh dear: %s had tried a dodgy bid of %d for a trick of size %d. The rest: %s",
                               player, bid, handSize, bids));
            }
        });
        LOGGER.info("Here are the bids: {}. Total={}, Trumps = {}",
                    bids, bids.values().stream().mapToInt(v -> (int) v).sum(), trumps);
    }

    private void deal(Integer number, Deck deck, Collection<Player> players) {
        LOGGER.info("Dealing {} cards each to the {} players...", number, players.size());
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
    private void giveHandsToPlayers(Collection<Player> players, List<List<Card>> hands) {
        Iterator<Player> itr = players.iterator();
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

    public Map<PlayerID, BidAndTaken> getBidAndTakens() {
        return bidAndTakens;
    }

    private class SetTrickLeadSuitFirstCardListener implements FirstCardListener {
        @Override
        public void onFirstCard(Trick trick, Card firstCard) {
            LOGGER.debug("Leading suit is {}", firstCard.suit());
            trick.setCardOrdering(setup.createTrickComparator(getTrumps(), firstCard.suit()));
        }
    }
}
