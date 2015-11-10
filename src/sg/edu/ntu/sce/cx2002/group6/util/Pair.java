package sg.edu.ntu.sce.cx2002.group6.util;

/**
 * {@code Pair} represents a pair of values.
 *
 * @param <First>  the type of the first value
 * @param <Second> the type of the second value
 */
public class Pair<First, Second> {

  /**
   * The first value.
   */
  public First first;
  /**
   * The second value.
   */
  public Second second;

  /**
   * Constructs a new pair.
   *
   * @param first  the first value
   * @param second the second value
   */
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