package net.declension.games.cards.ohhell;

import java.util.UUID;

import static java.lang.String.format;
import static net.declension.Utils.requireNonNullParam;

public class PlayerID {
    /**
     * The "size" of the ID space for pretty printing of numeric ID versions; should be small,
     * but enough to avoid realistic chances of collision.
     */
    private static final short PRETTY_DIGITS = 4;
    protected static final int PRETTY_SIZE = (int) Math.pow(10, PRETTY_DIGITS);
    private static final int HALF_PRETTY_SIZE = PRETTY_SIZE / 2;

    private final UUID uuid;

    /**
     * Construct a random player ID
     */
    public PlayerID() {
        this(UUID.randomUUID());
    }

    public PlayerID(UUID uuid) {
        requireNonNullParam(uuid, "UUID");
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerID)) {
            return false;
        }

        PlayerID other = (PlayerID) o;
        return uuid.equals(other.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return format("#%0" + PRETTY_DIGITS + "d",
                      uuid.getLeastSignificantBits() % HALF_PRETTY_SIZE + HALF_PRETTY_SIZE);
    }
}
