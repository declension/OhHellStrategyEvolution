package net.declension.games.cards;

import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;

public interface EndGameListener {

    void onGameEnd(Map<Player, Integer> scores);
}
