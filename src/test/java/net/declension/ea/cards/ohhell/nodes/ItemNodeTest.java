package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ItemNode.item;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemNodeTest {

    @Test
    public void equalsTest() {
        assertThat(item()).isEqualTo(item());
        assertThat(item()).isNotEqualTo(constant(5));
    }
}