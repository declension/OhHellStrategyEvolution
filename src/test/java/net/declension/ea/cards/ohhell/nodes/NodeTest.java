package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class NodeTest {

    static final int A_VALUE = 123;
    public static final ConstNode<Number, Object> CONST_NODE = new ConstNode<>(A_VALUE);
    static final int ANOTHER_VALUE = 999;

    @Test
    public void equalsShould() {
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