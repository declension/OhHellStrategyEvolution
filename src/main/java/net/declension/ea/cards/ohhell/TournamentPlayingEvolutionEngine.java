package net.declension.ea.cards.ohhell;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Tournament;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import net.declension.games.cards.ohhell.strategy.TrumpsBasedRandomStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class TournamentPlayingEvolutionEngine extends GenerationalEvolutionEngine<OhHellStrategy> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournamentPlayingEvolutionEngine.class);
    private final GameSetup gameSetup;
    private final int numberOfGames;

    public TournamentPlayingEvolutionEngine(CandidateFactory<OhHellStrategy> candidateFactory,
                                            EvolutionaryOperator<OhHellStrategy> evolutionScheme,
                                            SelectionStrategy<? super OhHellStrategy> selectionStrategy,
                                            GameSetup gameSetup, int numberOfGames) {
        super(candidateFactory, evolutionScheme, new PreComputedRankingFitnessEvaluator<>(), selectionStrategy, gameSetup.getRNG());
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
        return asList(new TrumpsBasedRandomStrategy(gameSetup.getRNG()));
    }
}
