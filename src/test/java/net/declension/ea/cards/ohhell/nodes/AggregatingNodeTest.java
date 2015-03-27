package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.nodes.AggregatingNode.Aggregator.*;
import static net.declension.ea.cards.ohhell.nodes.ConstNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AggregatingNodeTest {
    public static final List<Number> NUMBERS = asList(5, 1, 2, 5, -2.0, 3);
    private static final double NUMBERS_SUM = NUMBERS.stream().mapToDouble(Number::doubleValue).sum();
    public static final double NUMBERS_MEAN = NUMBERS_SUM / NUMBERS.size();
    public static final TestContext TEST_CONTEXT = new TestContext();
    public static final Number TEST_ITEM = mock(Number.class);

    @Test
    public void sumShouldWork() {
        Node<Number, TestContext> node = createNode(SUM);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(NUMBERS_SUM);
        assertThat(node.toString()).isEqualTo(format("sum(%s)", NUMBERS));
    }

    @Test
    public void meanShouldWork() {
        Node<Number, TestContext> node = createNode(MEAN);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(NUMBERS_MEAN);
        assertThat(node.toString()).contains("avg(");
    }

    @Test
    public void minShouldWork() {
        Node<Number, TestContext> node = createNode(MIN);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(-2.0);
        assertThat(node.toString()).contains("min(");
    }

    @Test
    public void maxShouldWork() {
        Node<Number, TestContext> node = createNode(MAX);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(5);
        assertThat(node.toString()).contains("max(");
    }

    @Test
    public void countShouldWork() {
        Node<Number, TestContext> node = createNode(COUNT);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(NUMBERS.size());
        assertThat(node.toString()).contains("count(").contains(NUMBERS.toString());
    }

    @Test
    public void varianceShouldWork() {
        Node<Number, TestContext> node = new AggregatingNode<>(VARIANCE, constant(2), constant(2), constant(5));
        // mean is 3.
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(1.0*1 + 1 * 1 + 2 * 2);
        assertThat(node.toString()).contains("var(");
    }


    private Node<Number, TestContext> createNode(AggregatingNode.Aggregator op) {
        Node<Number, TestContext> node = new AggregatingNode<>(op);

        List<? extends Node<Number, TestContext>> nodes = NUMBERS.stream()
                // Method references can't always be used, nor can type inferencing, it seems...
                .map(num -> new ConstNode<Number, TestContext>(num))
                .collect(toList());
        node.setChildren(nodes);
        return node;
    }

    private static class TestContext {
    }
}