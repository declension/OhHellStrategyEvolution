package net.declension.games.cards.tricks;

public class BidAndTaken {
    private final int bid;
    private final int score;

    public BidAndTaken(int bid, int score) {
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
