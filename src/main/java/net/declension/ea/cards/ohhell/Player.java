package net.declension.ea.cards.ohhell;

import java.util.UUID;

public class Player {
    private final UUID id;

    public Player() {
        this(UUID.randomUUID());
    }

    public Player(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return id.equals(player.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Player #%d", Math.abs(id.hashCode()) % 100000);
    }
}
