package net.declension.ea.cards.ohhell.ui;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.evolution.TournamentPlayingEvolutionEngine;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.swing.SwingBackgroundTask;
import org.uncommons.watchmaker.examples.AbstractExampleApplet;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.termination.Stagnation;
import org.uncommons.watchmaker.swing.AbortControl;
import org.uncommons.watchmaker.swing.ProbabilityParameterControl;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

import static net.declension.ea.cards.ohhell.OhHellStrategyEvolver.*;
import static org.uncommons.maths.random.Probability.EVENS;
import static org.uncommons.maths.random.Probability.ONE;

public class OhHellStrategyEvolverApplet extends AbstractExampleApplet {
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolverApplet.class);
    private JSpinner populationSpinner;
    private JSpinner elitismSpinner;
    private JButton startButton;
    private AbortControl abort;
    private EvolutionMonitor<GeneticStrategy> monitor;

    @Override
    protected void prepareGUI(Container container) {
        //AppletView view = new AppletView();
        //container.add(view, BorderLayout.CENTER);
        JPanel controls = new JPanel(new BorderLayout());
        controls.add(createParametersPanel(), BorderLayout.NORTH);
        container.add(controls, BorderLayout.NORTH);

        monitor = new EvolutionMonitor<>(new GeneticStrategyRenderer(), false);
        container.add(monitor.getGUIComponent(), BorderLayout.CENTER);
        //statusBar = new StatusBar();
        //container.add(statusBar, BorderLayout.SOUTH);
    }

    private JComponent createParametersPanel() {
        Box parameters = Box.createHorizontalBox();
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Population Size: "));
        parameters.add(Box.createHorizontalStrut(10));
        populationSpinner = new JSpinner(new SpinnerNumberModel(6, 2, 1000, 1));
        populationSpinner.setMaximumSize(populationSpinner.getMinimumSize());
        parameters.add(populationSpinner);
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Elitism: "));
        parameters.add(Box.createHorizontalStrut(10));
        elitismSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        elitismSpinner.setMaximumSize(elitismSpinner.getMinimumSize());
        parameters.add(elitismSpinner);
        parameters.add(Box.createHorizontalStrut(10));

        parameters.add(new JLabel("Selection Pressure: "));
        parameters.add(Box.createHorizontalStrut(10));
        ProbabilityParameterControl selectionPressureControl =
                new ProbabilityParameterControl(EVENS, ONE, 2, new Probability(0.7));
        parameters.add(selectionPressureControl.getControl());
        parameters.add(Box.createHorizontalStrut(10));

        startButton = new JButton("Start");
        abort = new AbortControl();
        startButton.addActionListener(ev -> {
            abort.getControl().setEnabled(true);
            new JLabel("Population Size: ").setEnabled(false);
            populationSpinner.setEnabled(false);
            new JLabel("Elitism: ").setEnabled(false);
            elitismSpinner.setEnabled(false);
            startButton.setEnabled(false);
            new EvolutionTask((Integer) populationSpinner.getValue(),
                              (Integer) elitismSpinner.getValue(),
                              abort.getTerminationCondition(),
                              new Stagnation(1000, false)
            ).execute();
        });
        abort.getControl().setEnabled(false);
        parameters.add(startButton);
        parameters.add(abort.getControl());
        parameters.add(Box.createHorizontalStrut(10));

        parameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
        return parameters;
    }

    public static void main(String[] args) {
        new OhHellStrategyEvolverApplet().displayInFrame("Oh hell strategy evolver");
    }


    /**
     * The task that actually performs the evolution.
     */
    private class EvolutionTask extends SwingBackgroundTask<GeneticStrategy> {
        private final int populationSize;
        private final int eliteCount;
        private final TerminationCondition[] terminationConditions;


        EvolutionTask(int populationSize, int eliteCount, TerminationCondition... terminationConditions) {
            this.populationSize = populationSize;
            this.eliteCount = eliteCount;
            this.terminationConditions = terminationConditions;
        }


        @Override
        protected GeneticStrategy performTask() throws Exception {
            int maxHandSize = 51 / (populationSize + TournamentPlayingEvolutionEngine.OUTSIDER_COUNT);
            GameSetup gameSetup = new GameSetup(() -> IntStream.rangeClosed(1, maxHandSize).boxed(), new StandardRules());

            EvolutionEngine<GeneticStrategy> engine = createEngine(gameSetup, GAMES_PER_TOURNAMENT);

            engine.addEvolutionObserver(createLoggingObserver());
            engine.addEvolutionObserver(monitor);

            // Go!
            List<EvaluatedCandidate<GeneticStrategy>> population
                    = engine.evolvePopulation(populationSize, eliteCount, terminationConditions);
            return population.get(0).getCandidate();
        }

        @Override
        protected void onError(Throwable throwable) {
            super.onError(throwable);
            postProcessing(null);
        }
    }
}
