package net.declension.ea.cards.ohhell.evolution;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Tournament;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class TournamentPlayingEvolutionEngine extends GenerationalEvolutionEngine<OhHellStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentPlayingEvolutionEngine.class);
    public static final int OUTSIDER_COUNT = 2;

    private final GameSetup gameSetup;
    private final int numberOfGames;

    public TournamentPlayingEvolutionEngine(GameSetup gameSetup, CandidateFactory<OhHellStrategy> candidateFactory,
                                            EvolutionaryOperator<OhHellStrategy> evolutionScheme,
                                            SelectionStrategy<? super OhHellStrategy> selectionStrategy,
                                            int numberOfGames
    ) {
        super(candidateFactory, evolutionScheme, new PreComputedFitnessEvaluator<>(), selectionStrategy,
              gameSetup.getRNG());
        this.gameSetup = gameSetup;
        this.numberOfGames = numberOfGames;
    }

    @Override
    protected List<EvaluatedCandidate<OhHellStrategy>> evaluatePopulation(List<OhHellStrategy> population) {

        List<OhHellStrategy> outsiders = createOutsiders();
        List<OhHellStrategy> totalPopulation = new ArrayList<>(population);
        totalPopulation.addAll(outsiders);
        List<Player> players = totalPopulation.stream()
                                              .map(s -> new BasicPlayer(gameSetup, s))
                                              .collect(toList());

        Tournament tournament = new Tournament(players, gameSetup);

        Map<Player, Double> results = tournament.playMultipleGamesSequentially(numberOfGames);
        return results.entrySet().stream()
                .filter(e -> !outsiders.contains(e.getKey().getStrategy()))
                .map(e -> new EvaluatedCandidate<>(e.getKey().getStrategy(), e.getValue()))
                .collect(toList());
    }

    private List<OhHellStrategy> createOutsiders() {
        List<OhHellStrategy> shortList = asList(new AverageSimpleStrategy(gameSetup),
                                                new AverageRandomStrategy(gameSetup.getRNG()),
                                                new TrumpsBasedRandomStrategy(gameSetup.getRNG()),
                                                new TrumpsBasedSimpleStrategy(gameSetup),
                                                new RandomRandomStrategy(gameSetup.getRNG()));
        Collections.shuffle(shortList);
        return shortList.subList(0, OUTSIDER_COUNT);
    }
}
