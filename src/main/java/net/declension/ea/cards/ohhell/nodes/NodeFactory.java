package net.declension.ea.cards.ohhell.nodes;

import net.declension.collections.WeightedEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedList;
import static net.declension.collections.CollectionUtils.pickRandomly;
import static net.declension.collections.WeightedEntry.weightedEntry;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.ALL_BINARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ALL_UNARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;

public class NodeFactory<I, C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);
    private static final int MAX_ARITY = 10;
    private static final int MAX_INT_RANGE = 10_000;
    private final Random rng;
    private final List<WeightedEntry<Supplier<Node<I, C>>>> weightedNodeMap;

    public NodeFactory(Random rng) {
        this.rng = rng;
        weightedNodeMap = createNodeMap();
    }

    public Node<I, C> createEphemeralRandom() {
        return new ConstNode<>(rng.nextInt(MAX_INT_RANGE));
    }

    public static <I, C> Node<I, C> createBinary(BinaryNode.Operator op, Node<I, C> left, Node<I, C> right) {
        BinaryNode<I, C> ret = new BinaryNode<>(op);
        return ret.setChildren(asList(left, right));
    }

    public Node<I, C> createRandomTree(int maxDepth) {
        if (maxDepth == 0) {
            return createTerminalNode();
        }
        Node<I, C> node = createRawRandomNode();
        List<Node<I, C>> children = synchronizedList(new ArrayList<>());
        Integer arity = node.arity().orElse(rng.nextInt(MAX_ARITY));
        IntStream.rangeClosed(1, arity)
                 .forEach(i -> children.add(createRandomTree(maxDepth - 1)));
        node.setChildren(children);
        return node;
    }

    private Node<I, C> createTerminalNode() {
        return createEphemeralRandom();
    }

    private Node<I, C> createRawRandomNode() {
        // TODO: weighted choosing.
        return pickRandomly(rng, weightedNodeMap).getValue().get();
    }

    private List<WeightedEntry<Supplier<Node<I, C>>>> createNodeMap() {
        List<WeightedEntry<Supplier<Node<I, C>>>> suppliers = new ArrayList<>();
        suppliers.add(weightedEntry(1, this::createEphemeralRandom));
        ALL_BINARY_OPERATORS.stream()
                .forEach(op -> suppliers.add(weightedEntry(1, () -> binary(op))));
        ALL_UNARY_OPERATORS.stream()
                .forEach(op -> suppliers.add(weightedEntry(1, () -> unary(op))));
        LOGGER.info("Factory node map: {}", suppliers);
        return suppliers;
    }

    private static <I,C>  BinaryNode<I, C> binary(BinaryNode.Operator op) {
        return new BinaryNode<>(op);
    }

}
