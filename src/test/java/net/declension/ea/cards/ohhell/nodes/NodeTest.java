package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.ConstNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class NodeTest {

    static final int A_VALUE = 123;
    static final ConstNode<Object, Object> CONST_NODE = new ConstNode<>(A_VALUE);
    static final int ANOTHER_VALUE = 999;
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeTest.class);

    @Test
    public void replaceShouldRemoveChildrenAndSetUpNewNodeWithExisting() {
        Node<Object, Object> parent = new BinaryNode<>(BinaryNode.Operator.DIVIDE);
        Node<Object, Object> node = new BinaryNode<>(BinaryNode.Operator.MULTIPLY);
        parent.addChild(node);
        parent.addChild(CONST_NODE);
        Node<Object, Object> child = constant(2);
        Node<Object, Object> anotherChild = new UnaryNode<>(UnaryNode.Operator.ABS);
        anotherChild.addChild(constant(-3));
        node.setChildren(asList(child, anotherChild));
        LOGGER.info("Original version: {}", parent);

        Node<Object, Object> replacement = new BinaryNode<>(BinaryNode.Operator.ADD);
        // Simulate a mutation from * -> +
        replacement.setChildren(asList(child, anotherChild));
        // Go
        node.replaceWith(replacement);

        assertThat(node.children()).isEmpty();
        // The parent should contain this, but also the other child
        assertThat(parent.children).containsExactly(replacement, CONST_NODE);
        LOGGER.info("Replaced version: {}", parent);
    }

    @Test
    public void nameShould() {


    }

    @Test
    public void equalsTest() {
        assertThat(CONST_NODE).isEqualTo(new ConstNode<>(A_VALUE));
        assertThat(CONST_NODE).isNotEqualTo(new ConstNode<>(ANOTHER_VALUE));
        assertThat(CONST_NODE).isNotEqualTo(new RandomNode<>(mock(Random.class)));
        assertThat(CONST_NODE).isNotEqualTo(null);
    }

    @Test
    public void hashCodeShouldWork() {
        assertThat(CONST_NODE.hashCode()).isNotEqualTo(new ConstNode<>(ANOTHER_VALUE).hashCode());
    }

    @Test
    public void hashCodeShouldBeUniqueAcrossSubClassesToo() {
        assertThat(CONST_NODE.hashCode()).isNotEqualTo(new RandomNode<>(mock(Random.class)).hashCode());
    }
}