package net.declension.ea.cards.ohhell.nodes;

import java.util.Optional;

public abstract class TerminalNode<I, C> extends Node<I, C> {

    @Override
    public Optional<Integer> arity() {
        return Optional.of(0);
    }
}
