package net.declension.ea.cards.ohhell.nodes;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class NodeFactoryTest {
    private static final Integer RANDOM_INT = 37;
    private static final Integer[] RANDOM_VALUES = new Integer[]{RANDOM_INT, 2, 3, 57, 2 ,1, 0, 4, 3, 2, 1, 0, 72, 2,
                                                                 1, 4, 55, 4, 2, 1, new Random().nextInt(5)};
    private NodeFactory factory;
    private TestRandom rng;

    @Before
    public void setUp() throws Exception {
        rng = new TestRandom();
        factory = new NodeFactory(rng);
    }

    @Test
    public void createEphemeralRandomShouldUseTheRNG() {
        rng.reset();
        Node<Object, Object> node = factory.createEphemeralIntegerRandom();

        assertThat(node).isInstanceOf(ConstantNode.class);
        int expected = 2 - NodeFactory.MAX_INT_RANGE / 2;
        assertThat(node.evaluate(mock(Number.class), mock(Object.class))).isEqualTo(expected);
    }

    @Test
    public void createRandomTreeForZeroShouldMakeATerminalNode() {
        Node<Integer, Object> tree = factory.createRandomTree(0, 0);
        assertThat(tree.children()).isEmpty();
        assertThat(tree).isInstanceOf(TerminalNode.class);
    }

    @Test
    public void createRandomTreeForOneShouldMakeOneDeep() {
        Node<Integer, Object> tree = factory.createRandomTree(1, 1);
        assertThat(tree.children()).isNotEmpty();
        assertThat(tree.child(0).arity().get()).isEqualTo(0);
    }

    @Test
    public void createRandomTreeForRangeShouldMakeSomething() {
        Node<Integer, Object> tree = factory.createRandomTree(1, 3);
        assertThat(tree.children()).isNotEmpty();
        assertThat(tree.depth()).isBetween(1, 3);
    }

    @Test
    public void createRandomTreeShouldProduceTreesOfSuitableDepth() {
        Node<Integer, Object> tree = factory.createRandomTree(3, 3);
        assertThat(tree.children.size()).isGreaterThanOrEqualTo(tree.arity().orElse(2));
        //assertThat(tree.countNodes()).isGreaterThan(3);
        assertThat(tree.depth()).isEqualTo(3);
    }

    @Test
    public void createRandomTreeShouldThrowForMinLargerThanMax() {
        assertThatThrownBy(() -> factory.createRandomTree(2, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    static class TestRandom extends Random {

        private static final Logger LOGGER = LoggerFactory.getLogger(TestRandom.class);
        private static int index;
        static final int SIZE = RANDOM_VALUES.length;

        void reset() {
            index = 0;
        }

        @Override
        public int nextInt() {
            throw new UnsupportedOperationException("Nope");
        }

        @Override
        public int nextInt(int bound) {
            int cur;
            int start = index;
            while ((cur = RANDOM_VALUES[index]) >= bound) {
                index = (index + 1) % SIZE;
                if (index == start) {
                    throw new IllegalArgumentException("Can't find an int to match " + bound);
                }
            }
            index = (index + 1) % SIZE;
            return cur;
        }
    }
}