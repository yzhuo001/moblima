package util;

public class Pair<First, Second> {

  public First first;
  public Second second;

  public Pair(First first, Second second) {
    this.first = first;
    this.second = second;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pair)) return false;

    Pair<?, ?> pair = (Pair<?, ?>) o;

    return !(first != null ? !first.equals(pair.first) : pair.first != null) && !(second != null ? !second.equals(pair.second) : pair.second != null);
  }

  @Override
  public String toString() {
    return first.toString() + second.toString();
  }

  @Override
  public int hashCode() {
    int result = first != null ? first.hashCode() : 0;
    result = 31 * result + (second != null ? second.hashCode() : 0);
    return result;
  }
}