package net.declension.ea.cards.ohhell.nodes;

import java.util.Random;

import static java.util.Arrays.asList;

public class NodeFactory {

    private static Random rng;

    public static <T> Node<T> create() {
        return new ConstNode<>(rng.nextInt());
    }

    public static <T> Node<T> createBinary(BinaryNode.Operator op, Node<T> left, Node<T> right) {
        BinaryNode<T> ret = new BinaryNode<>(op);
        return ret.setChildren(asList(left, right));
    }
}
