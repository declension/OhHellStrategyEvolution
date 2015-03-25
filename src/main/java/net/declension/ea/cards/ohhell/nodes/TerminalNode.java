package net.declension.ea.cards.ohhell.nodes;

import java.util.Optional;

public abstract class TerminalNode<T> extends Node<T> {

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }
}
