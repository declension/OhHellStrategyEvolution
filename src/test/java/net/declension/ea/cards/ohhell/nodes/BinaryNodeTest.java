package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.ADD;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.EXPONENTIATE;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.MULTIPLY;
import static net.declension.ea.cards.ohhell.nodes.ConstNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class BinaryNodeTest {

    public static final TestContext TEST_CONTEXT = new TestContext();
    private static final Number DUMMY_ITEM = mock(Number.class);

    @Test
    public void evaluateShouldThrowForMissingChildren() {
        Node<Number, TestContext> node = new BinaryNode<>(ADD);
        assertThatThrownBy(() -> node.evaluate(DUMMY_ITEM, TEST_CONTEXT))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("0 children");
    }

    @Test
    public void evaluateShouldAddConstants() {
        Node<Number, TestContext> node = new BinaryNode<>(ADD, constant(1), constant(2));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(3.0);
        assertThat(node.toString()).isEqualTo("(1 + 2)");
    }


    @Test
    public void evaluateShouldEvaluateWholeTree() {
        Node<Number, TestContext> node = new BinaryNode<>(ADD);
        Node<Number, TestContext> subNode = new BinaryNode<>(MULTIPLY);
        subNode.setChildren(asList(constant(3), constant(4)));
        node.setChildren(asList(constant(1), subNode));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(1.0 + (3 * 4));
        assertThat(node.toString()).isEqualTo("(1 + (3 * 4))");
    }

    @Test
    public void exponentiateWorksToo() {
        Node<Number, TestContext> node = new BinaryNode<>(EXPONENTIATE, constant(2), constant(3));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(8.0);
        assertThat(node.toString()).isEqualTo("(2 ^ 3)");
    }


    /**
     * Dummy context
     */
    private static class TestContext {
    }
}