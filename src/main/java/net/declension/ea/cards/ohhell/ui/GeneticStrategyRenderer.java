package net.declension.ea.cards.ohhell.ui;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import org.uncommons.watchmaker.framework.interactive.Renderer;

import javax.swing.*;

public class GeneticStrategyRenderer implements Renderer<GeneticStrategy, JComponent> {

    /**
     * @param strategy The evolved entity to render.
     * @return A text area containing the string representation of the entity.
     */
    public JComponent render(GeneticStrategy strategy) {
        JTextArea text = new JTextArea(strategy.fullDetails());
        text.setEditable(false);
        text.setBackground(null);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        return text;
    }

}
