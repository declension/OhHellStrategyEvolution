package net.declension.games.cards.ohhell.player;

import java.util.UUID;

import static java.lang.Math.min;
import static java.lang.String.format;
import static net.declension.utils.Validation.requireNonNullParam;

public class PlayerID {
    /**
     * The "size" of the ID space for pretty printing of numeric ID versions; should be small,
     * but enough to avoid realistic chances of collision.
     */
    private static final short PRETTY_DIGITS = 14;

    private final String uid;

    /**
     * Construct a random player ID
     */
    public PlayerID() {
        this(UUID.randomUUID().toString());
    }

    public PlayerID(String uid) {
        requireNonNullParam(uid, "UID");
        this.uid = uid;
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
        return uid.equals(other.uid);

    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public String toString() {
        return format("<%s>", uid.substring(0, min(PRETTY_DIGITS, uid.length())));
    }
}
