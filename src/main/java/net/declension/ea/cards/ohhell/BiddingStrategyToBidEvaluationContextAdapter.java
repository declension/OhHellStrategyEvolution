package net.declension.ea.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static net.declension.games.cards.Suit.ALL_SUITS;

public class BiddingStrategyToBidEvaluationContextAdapter implements BidEvaluationContext {

    private final List<RankRanking> trumpsRanks;
    private final List<List<RankRanking>> otherRanksList;
    private final List<Range> bidsSoFar;
    private final Range remainingBids;
    private final GameSetup gameSetup;
    private Range proposedBid;
    private final int tricksThisRound;

    public BiddingStrategyToBidEvaluationContextAdapter(GameSetup gameSetup, Optional<Suit> trumps, Player me,
                                                        Set<Card> myCards, AllBids bidsSoFar,
                                                        Set<Integer> allowedBids) {
        tricksThisRound = myCards.size();
        int numPlayers = bidsSoFar.capacity();
        this.gameSetup = gameSetup;
        trumpsRanks = trumpsRanksFor(myCards, trumps);
        remainingBids = new Range(bidsSoFar.remaining(), 0, numPlayers);
        this.bidsSoFar = rangedBidsFor(bidsSoFar, tricksThisRound);
        otherRanksList = nonTrumpsRanksFor(myCards, trumps);
    }

    private List<Range> rangedBidsFor(AllBids bidRanges, int max) {
        return bidRanges.values().stream()
                                 .filter(Optional::isPresent)
                                 .map(bid -> new Range(bid.get(), 0, max))
                                 .collect(toList());
    }

    private List<RankRanking> trumpsRanksFor(Set<Card> myCards, Optional<Suit> trumps) {
        return trumps.isPresent()? filteredSortedCards(myCards, c -> c.suit() == trumps.get()) : emptyList();
    }

    private List<List<RankRanking>> nonTrumpsRanksFor(Set<Card> myCards, Optional<Suit> trumps) {
        return ALL_SUITS.stream()
                .filter(s -> !trumps.isPresent() || trumps.get() == s)
                .map(s -> filteredSortedCards(myCards, c -> c.suit() == s))
                .collect(toList());
    }

    private List<RankRanking> filteredSortedCards(Set<Card> myCards, Predicate<Card> predicate) {
        return myCards.stream()
                      .filter(predicate)
                      .map(c -> new RankRanking(c.rank(), gameSetup.getRankComparator()))
                      .sorted()
                      .collect(toList());
    }

    @Override
    public Range proposedBid() {
        return proposedBid;
    }

    @Override
    public List<RankRanking> getTrumpsRanks() {
        return trumpsRanks;
    }

    @Override
    public List<List<RankRanking>> getOtherRanks() {
        return otherRanksList;
    }

    @Override
    public List<Range> getBidsSoFar() {
        return bidsSoFar;
    }

    @Override
    public Range getRemainingBidCount() {
        return remainingBids;
    }

    @Override
    public BidEvaluationContext withProposedBid(Integer bid) {
        proposedBid = new Range(bid, 0, tricksThisRound);
        return this;
    }
}
