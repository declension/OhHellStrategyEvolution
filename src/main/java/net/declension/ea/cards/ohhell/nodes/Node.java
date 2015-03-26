package net.declension.ea.cards.ohhell.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public abstract class Node<T> {
    protected List<Node<T>> children = new ArrayList<>();

    public final Number evaluate(T context) {
        checkChildren();
        return doEvaluation(context);
    }

    protected abstract Number doEvaluation(T context);

    protected void checkChildren() {
        arity().ifPresent(a -> {
            int numChildren = children.size();
            if (numChildren != a) {
                throw new IllegalStateException(
                        format("Can't evaluate %s(%d) with %d child%s!",
                               getClass().getSimpleName(), a, numChildren, numChildren == 1? "" : "ren"));
            }
        });
    }

    public List<Node<T>> children() {
        return children;
    }

    public Node<T> setChildren(List<? extends Node<T>> children) {
        this.children = new ArrayList<>(children);
        return this;
    }

    public abstract Optional<Integer> arity();
}
