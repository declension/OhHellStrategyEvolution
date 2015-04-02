package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import java.util.Random;

import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ConstantNodeTest {

    public static final Node<Object, Object> MINUS_3 = constant(-3);

    @Test
    public void evaulateShouldReturn() {
        Node<Object, Object> node = MINUS_3;
        assertThat(node.evaluate(0, 0)).isEqualTo(-3);
    }

    @Test
    public void mutatedCopyShouldApplyPercentageChangeKeepingType() {
        Random rng = mock(Random.class);
        assertThat(constant(60.0).mutate(rng).evaluate(null, null)).isEqualTo(30.0);
    }

    @Test
    public void equalsShouldWorkNormally() {
        assertThat(MINUS_3).isEqualTo(constant(-3));
        assertThat(MINUS_3).isEqualTo(MINUS_3);
    }

    @Test
    public void allChildrenAreConstantsShouldReturnTrue() {
        assertThat(constant(5).effectivelyConstant()).isTrue();
    }
}