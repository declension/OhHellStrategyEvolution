package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.Range;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class UnaryNodeTest {

    private UnaryNode<Integer, Object> ln;
    public static final double E_TO_FIVE_ISH = 148.414;

    @Before
    public void setUp() throws Exception {
        ln = new UnaryNode<>(UnaryNode.Operator.LN);
    }

    @Test
    public void evaluateShouldWorkForLog() {
        ln.addChild(constant(E_TO_FIVE_ISH));
        assertThat(ln.evaluate(0, mock(Object.class)).intValue()).isEqualTo(5);
    }

    @Test
    public void lnShouldNotBlowUpForZero() {
        ln.addChild(constant(0));
        ln.evaluate(0, mock(Object.class));
    }

    @Test
    public void evaluateShouldWorkForAbs() {
        UnaryNode<Integer, Object> node = new UnaryNode<>(UnaryNode.Operator.ABS);;
        node.addChild(constant(-3));
        assertThat(node.evaluate(0, mock(Object.class))).isEqualTo(3);
    }

    @Test
    public void evaluateShouldWorkForFloor() {
        UnaryNode<Integer, Object> node = new UnaryNode<>(UnaryNode.Operator.FLOOR);;
        node.addChild(constant(2.6));
        assertThat(node.evaluate(0, mock(Object.class))).isEqualTo(2.0);
    }

    @Test
    public void mutatedCopyShouldChangeOperator() {
        UnaryNode<Integer, Object> node = new UnaryNode<>(UnaryNode.Operator.ABS);
        node.addChild(constant(3));
        node.mutate(mock(Random.class));
        assertThat(node.getOperator()).isEqualTo(UnaryNode.Operator.LN);
        assertThat(node.children).containsExactly(constant(3));
    }

    @Test
    public void simplifyShouldReduceConstants() {
        UnaryNode<Integer, Object> node = new UnaryNode<>(UnaryNode.Operator.ABS);
        node.addChild(constant(-3));
        Node newNode = node.simplifiedVersion();
        assertThat(newNode).isEqualTo(constant(3));
    }

    @Test
    public void simplifyShouldReduceMultipleAbs() {
        UnaryNode<Range, Object> node = new UnaryNode<>(UnaryNode.Operator.ABS);
        UnaryNode<Range, Object> child = new UnaryNode<>(UnaryNode.Operator.ABS);
        node.addChild(child);
        child.addChild(new ItemNode<>());
        assertThat(node.toString()).contains("abs(abs(x))");
        assertThat(node.simplifiedVersion().toString()).contains("abs(x)");
    }

    @Test
    public void simplifyShouldReduceMultipleFloor() {
        UnaryNode<Range, Object> node = new UnaryNode<>(UnaryNode.Operator.FLOOR);
        UnaryNode<Range, Object> child = new UnaryNode<>(UnaryNode.Operator.FLOOR);
        node.addChild(child);
        child.addChild(new ItemNode<>());
        assertThat(node.toString()).contains("floor(floor(x))");
        assertThat(node.simplifiedVersion().toString()).contains("floor(x)");
    }
}