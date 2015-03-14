package net.declension.games.cards.ohhell;

import net.declension.games.cards.tricks.BidAndTaken;

import java.util.function.Function;

interface Scorer extends Function<BidAndTaken, Short> {

}
