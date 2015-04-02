package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.utils.StringUtils;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.declension.ea.cards.ohhell.nodes.AggregatingNode.Aggregator.*;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AggregatingNodeTest {
    static final List<Number> NUMBERS = asList(5, 1, 2, 5, -2.0, 3);
    static final String PRETTY_NUMBERS_STRING = NUMBERS.stream().map(StringUtils::tidyNumber)
                                                              .collect(toList()).toString();
    static final Double NUMBERS_SUM = NUMBERS.stream().mapToDouble(Number::doubleValue).sum();
    static final Double NUMBERS_MEAN = NUMBERS_SUM / NUMBERS.size();
    static final TestContext TEST_CONTEXT = new TestContext();
    static final Number TEST_ITEM = mock(Number.class);
    static final List<Node<Number, TestContext>> TEST_VALUES
            = asList(constant(2), constant(1), constant(3), constant(5.0));
    static final List<Node<Number, TestContext>> TEST_NODES
            = NUMBERS.stream()
                     // Method references can't always be used, nor can type inferencing, it seems...
                     .map(num -> new ConstantNode<Number, TestContext>(num))
                     .collect(toList());
    private static final Node<Number, TestContext> TEST_NODE = constant(123.45);

    @Test
    public void sumShouldWork() {
        Node<Number, TestContext> node = createNode(SUM);
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(NUMBERS_SUM);
        assertThat(node.toString()).isEqualTo(format("sum(%s)", PRETTY_NUMBERS_STRING));
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
        assertThat(node.toString()).contains("count(").contains(PRETTY_NUMBERS_STRING);
    }

    @Test
    public void varianceShouldWork() {
        Node<Number, TestContext> node = new AggregatingNode<>(VARIANCE, constant(2), constant(2), constant(5));
        // mean is 3.
        assertThat(node.evaluate(TEST_ITEM, TEST_CONTEXT)).isEqualTo(1.0*1 + 1 * 1 + 2 * 2);
        assertThat(node.toString()).contains("var(");
    }


    @Test
    public void simplifyShouldReduceConstants() {
        Node<Number, TestContext> node = createNode(MAX);
        node.setChildren(TEST_VALUES);
        assertThat(node.simplifiedVersion()).isEqualTo(constant(5.0));
    }

    @Test
    public void simplifyShouldReduceSingleAggregates() {
        ALL_AGGREGATORS.stream()
                       .filter(a -> a != VARIANCE && a != COUNT )
                       .map(this::createSingleChildNode)
                       .forEach(n -> assertThat(n.simplifiedVersion()).isEqualTo(TEST_NODE));
    }

    @Test
    public void varianceShouldNotReduceWithMultipleChildren() {
        Node<Number, TestContext> node = new AggregatingNode<>(VARIANCE);
        node.setChildren(asList((Node) new ItemNode(), constant(0)));
        assertThat(node.simplifiedVersion().child(0)).isEqualTo(new ItemNode());
    }

    @Test
    public void varianceShouldReduceWithOneChild() {
        Node<Range, BidEvaluationContext> node = new AggregatingNode<>(VARIANCE);
        node.addChild(new ItemNode());
        assertThat(node.simplifiedVersion()).isEqualTo(constant(0));
    }

    @Test
    public void simplifyShouldReduceSingleChildVariables() {
        Node<Number, TestContext> node = new AggregatingNode<>(MAX);
        node.addChild(new RandomNode<>(null));
        assertThat(node.simplifiedVersion()).isEqualTo(new RandomNode<>(null));
    }


    private Node<Number, TestContext> createNode(AggregatingNode.Aggregator op) {
        Node<Number, TestContext> node = new AggregatingNode<>(op);
        node.setChildren(TEST_NODES);
        return node;
    }

    private Node<Number, TestContext> createSingleChildNode(AggregatingNode.Aggregator op) {
        Node<Number, TestContext> node = new AggregatingNode<>(op);
        node.addChild(TEST_NODE);
        return node;
    }

    private static class TestContext {
    }
}