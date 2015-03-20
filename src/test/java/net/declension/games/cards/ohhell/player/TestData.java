package net.declension.games.cards.ohhell.player;

import net.declension.games.cards.Card;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;

import java.util.Collection;

import static java.util.Arrays.asList;

public final class TestData {

    public static final Player ALICE = new DummyPlayer("Alice");
    public static final Player BOB = new DummyPlayer("Bob");
    public static final Player CHARLIE = new DummyPlayer("Charlie");
    public static final Player DANIELLE = new DummyPlayer("Danielle");
    public static final Collection<Player> PLAYERS = asList(ALICE, BOB, CHARLIE, DANIELLE);

    public static final Card ACE_OF_SPADES = new Card(Rank.ACE, Suit.SPADES);
    public static final Card ACE_OF_HEARTS = new Card(Rank.ACE, Suit.HEARTS);
    public static final Card TWO_OF_DIAMONDS = new Card(Rank.TWO, Suit.DIAMONDS);
    public static final Card TWO_OF_CLUBS = new Card(Rank.TWO, Suit.CLUBS);
    public static final Card QUEEN_OF_DIAMONDS = new Card(Rank.QUEEN, Suit.DIAMONDS);
}
