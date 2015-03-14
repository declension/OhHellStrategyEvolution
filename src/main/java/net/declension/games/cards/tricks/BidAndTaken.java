package net.declension.games.cards.tricks;

public class BidAndTaken {
    private final short bid;
    private final short score;

    public BidAndTaken(short bid, short score) {
        this.bid = bid;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidAndTaken)) return false;

        BidAndTaken that = (BidAndTaken) o;
        return bid == that.bid && score == that.score;
    }

    @Override
    public int hashCode() {
        int result = bid;
        result = 31 * result + score;
        return result;
    }
}
