package net.declension.ea.cards.ohhell.ui;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import org.uncommons.watchmaker.framework.interactive.Renderer;

import javax.swing.*;
import java.awt.*;

public class GeneticStrategyRenderer implements Renderer<GeneticStrategy, JComponent> {

    /**
     * @param strategy The evolved entity to render.
     * @return A text area containing the string representation of the entity.
     */
    public JComponent render(GeneticStrategy strategy) {
        JTextArea text = new JTextArea(getStrategyText(strategy));
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        text.setBackground(null);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        return text;
    }

    private String getStrategyText(GeneticStrategy strategy) {
        return strategy.fullDetails() + "\n\nSimplified to:\n\n" + strategy.getBidEvaluator().simplifiedVersion();
    }

}
