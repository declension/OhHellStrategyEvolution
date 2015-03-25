package net.declension.ea.cards.ohhell.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public abstract class Node<T> {
    protected List<Node<T>> children = new ArrayList<>();

    public abstract Number evaluate(T context);

    protected void checkChildren() {
        arity().ifPresent(a -> {
            if (children.size() != a) {
                throw new IllegalStateException(format("Can't evaluate %s with %d children!",
                                                       getClass().getSimpleName(), children.size()));
            }
        });
    }

    public List<Node<T>> children() {
        return children;
    }

    Node<T> setChildren(List<? extends Node<T>> children) {
        this.children = new ArrayList<>(children);
        return this;
    }

    public abstract Optional<Integer> arity();
}
