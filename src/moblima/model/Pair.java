package moblima.model;

class Pair<First, Second> {

    private final First first;
    private final Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        return !(getFirst() != null ? !getFirst().equals(pair.getFirst()) : pair.getFirst() != null) && !(getSecond() != null ? !getSecond().equals(pair.getSecond()) : pair.getSecond() != null);

    }

    @Override
    public int hashCode() {
        int result = getFirst() != null ? getFirst().hashCode() : 0;
        result = 31 * result + (getSecond() != null ? getSecond().hashCode() : 0);
        return result;
    }
}