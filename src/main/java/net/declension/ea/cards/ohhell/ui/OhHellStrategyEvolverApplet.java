package net.declension.ea.cards.ohhell.ui;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.evolution.*;
import net.declension.games.cards.ohhell.GameSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.swing.SwingBackgroundTask;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.Replacement;
import org.uncommons.watchmaker.framework.termination.Stagnation;
import org.uncommons.watchmaker.swing.AbortControl;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static java.util.Arrays.asList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static net.declension.ea.cards.ohhell.OhHellStrategyEvolver.*;

/**
 * Entry point for the evolver, using an extension of Watchmaker's Swing-based GUI.
 */
public class OhHellStrategyEvolverApplet extends JApplet {
    private static final Logger LOGGER = LoggerFactory.getLogger(OhHellStrategyEvolverApplet.class);
    public static final int STAGNANT_GENERATION_LIMIT = 500;
    private static final Probability CROSSOVER_PROBABILITY = new Probability(0.1);

    /**
     * Some UI default values.
     */
    static class Defaults {
        static final int POPULATION_SIZE = 5;
        static final int ELITES = 1;
        static final int TOURNAMENT_SIZE = 15;
        static final double P_REPLACEMENT = 0.1;
    }

    private JSpinner populationSpinner;
    private JSpinner replacementProbControl;
    private JSpinner elitismSpinner;
    private JButton startButton;
    private AbortControl abort;
    private EvolutionMonitor<GeneticStrategy> monitor;
    private JSpinner tournamentSizeSpinner;

    @Override
    public void init() {
        configure(this);
    }

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

    /**
     * Configure the program to display its GUI in the specified container.
     * @param container The container to place the GUI components in.
     */
    private void configure(final Container container) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    // This should never happen as we are installing a known look-and-feel.
                    LOGGER.error("Failed to load System look-and-feel.", ex);
                }
                prepareGUI(container);
            });
        } catch (InterruptedException ex) {
            // Restore interrupt flag.
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException ex) {
            LOGGER.error("Couldn't start up Swing UI", ex);
            JOptionPane.showMessageDialog(container, ex.getCause(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComponent createParametersPanel() {
        Box parameters = Box.createHorizontalBox();

        addPopulationSizeControl(parameters);
        addElitismControl(parameters);
        //addSelectionPressureControl(parameters);
        addTournamentSizeControl(parameters);
        addReplacementProbabilityControl(parameters);
        addButtons(parameters);

        parameters.setBorder(BorderFactory.createTitledBorder("Parameters"));
        return parameters;
    }

    private void addButtons(Box parameters) {
        parameters.add(Box.createHorizontalStrut(10));
        startButton = new JButton("Start");
        abort = new AbortControl();
        startButton.addActionListener(ev -> {
            abort.getControl().setEnabled(true);
            populationSpinner.setEnabled(false);
            elitismSpinner.setEnabled(false);
            startButton.setEnabled(false);
            new EvolutionTask((int) populationSpinner.getValue(),
                              (int) tournamentSizeSpinner.getValue(), (int) elitismSpinner.getValue(),
                              new Probability((double) replacementProbControl.getValue()),
                              abort.getTerminationCondition(),
                              new Stagnation(STAGNANT_GENERATION_LIMIT, false)
            ).execute();
        });
        abort.getControl().setEnabled(false);
        parameters.add(startButton);
        parameters.add(abort.getControl());
        parameters.add(Box.createHorizontalStrut(10));
    }

    private void addPopulationSizeControl(Box parameters) {
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Population Size: "));
        parameters.add(Box.createHorizontalStrut(10));
        populationSpinner = new JSpinner(new SpinnerNumberModel(Defaults.POPULATION_SIZE, 2, 1000, 1));
        populationSpinner.setMaximumSize(populationSpinner.getMinimumSize());
        parameters.add(populationSpinner);
    }

    private void addElitismControl(Box parameters) {
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Elitism: "));
        parameters.add(Box.createHorizontalStrut(10));
        elitismSpinner = new JSpinner(new SpinnerNumberModel(Defaults.ELITES, 0, 4, 1));
        elitismSpinner.setMaximumSize(elitismSpinner.getMinimumSize());
        parameters.add(elitismSpinner);
    }

    private void addTournamentSizeControl(Box parameters) {
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Tournament Size: "));
        parameters.add(Box.createHorizontalStrut(10));
        tournamentSizeSpinner = new JSpinner(new SpinnerNumberModel(Defaults.TOURNAMENT_SIZE, 2, 100, 1));
        tournamentSizeSpinner.setMaximumSize(tournamentSizeSpinner.getMinimumSize());
        parameters.add(tournamentSizeSpinner);
    }

    private void addReplacementProbabilityControl(Box parameters) {
        parameters.add(Box.createHorizontalStrut(10));
        parameters.add(new JLabel("Replacement probability: "));
        parameters.add(Box.createHorizontalStrut(10));
        replacementProbControl = new JSpinner(new SpinnerNumberModel(Defaults.P_REPLACEMENT, 0.0, 1.0, 0.05));
        tournamentSizeSpinner.setMaximumSize(tournamentSizeSpinner.getMinimumSize());
        parameters.add(replacementProbControl);
    }

    public static void main(String[] args) {
        new OhHellStrategyEvolverApplet().displayInFrame("Oh hell strategy evolver");
    }

    private void displayInFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        configure(frame);
        //frame.pack();
        frame.setSize(1200, 900);
        frame.setVisible(true);
    }


    /**
     * The task that actually performs the evolution.
     */
    private class EvolutionTask extends SwingBackgroundTask<GeneticStrategy> {
        private final int populationSize;
        private final int eliteCount;
        private final TerminationCondition[] terminationConditions;
        private final GameSetup gameSetup;
        private final GeneticStrategyFactory candidateFactory;
        private final int tournamentSize;
        private final EvolutionaryOperator<GeneticStrategy> evolution;


        EvolutionTask(int populationSize, int tournamentSize, int eliteCount, Probability replacementProbability,
                      TerminationCondition... terminationConditions) {
            this.populationSize = populationSize;
            this.tournamentSize = tournamentSize;
            this.eliteCount = eliteCount;

            this.terminationConditions = terminationConditions;
            gameSetup = defaultGameSetup();
            candidateFactory = new GeneticStrategyFactory(gameSetup, MAX_BID_NODE_DEPTH);
            evolution = createEvolution(candidateFactory, replacementProbability);
        }

        private EvolutionaryOperator<GeneticStrategy> createEvolution(GeneticStrategyFactory candidateFactory,
                                                                      Probability replacementProbability) {
            return new EvolutionPipeline<>(
                    asList(new Replacement<>(candidateFactory, replacementProbability),
                           new TreeMutation(MUTATION_PROBABILITY, NODE_MUTATION_PROBABILITY),
                           new TreeCrossover(CROSSOVER_PROBABILITY),
                           new Simplification(SIMPLIFICATION_PROBABILITY)));
        }

        @Override
        protected GeneticStrategy performTask() {
            EvolutionEngine<GeneticStrategy> engine = createEngine(gameSetup, tournamentSize, evolution,
                                                                   candidateFactory);

            engine.addEvolutionObserver(createLoggingObserver());
            engine.addEvolutionObserver(monitor);

            // Go!
            List<EvaluatedCandidate<GeneticStrategy>> population
                    = engine.evolvePopulation(populationSize, eliteCount, terminationConditions);
            return population.get(0).getCandidate();
        }

        @Override
        protected void postProcessing(GeneticStrategy result) {
            abort.reset();
            abort.getControl().setEnabled(false);
            populationSpinner.setEnabled(true);
            elitismSpinner.setEnabled(true);
            tournamentSizeSpinner.setEnabled(true);
            startButton.setEnabled(true);
        }

        @Override
        protected void onError(Throwable throwable) {
            super.onError(throwable);
            postProcessing(null);
        }
    }
}
