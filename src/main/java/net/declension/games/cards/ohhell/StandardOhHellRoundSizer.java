package net.declension.games.cards.ohhell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

public class StandardOhHellRoundSizer implements RoundSizer {
    @Override
    public List<Integer> getFor(int numPlayers) {
        int maxHandSize = 51 / numPlayers;
        List<Integer> ordered = rangeClosed(1, maxHandSize).boxed().collect(toList());
        List<Integer> full = new ArrayList<>(ordered);
        Collections.reverse(ordered);
        full.addAll(ordered.subList(1, maxHandSize));
        return full;
    }
}
