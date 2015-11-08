package moblima.model;

import util.IntArray2D;
import util.Pair;
import util.Streams;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Represents all information associated with a cinema: name, rank, and layout.
 */
public class Cinema implements Serializable {
  private static final int P = -1; //padding

  /**
   * The layout map where 0 is space, 1 is single seat, 2 is couple seat.
   */
  public static final IntArray2D LAYOUT = new IntArray2D(new int[]{
    0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
    2, P, 2, P, 2, P, 2, P, 0, 2, P, 2, P, 2, P, 2, P,
    2, P, 2, P, 2, P, 2, P, 0, 2, P, 2, P, 2, P, 2, P,
    2, P, 2, P, 2, P, 2, P, 0, 0, 0, 1, 1, 2, P, 2, P,
  }, 17);

  private static Integer seatCount;

  private int id;
  private Rank rank;
  private String name;

  /**
   * Gets the total number of seats available in this cinema.
   *
   * @return the seat count
   */
  public static int getSeatCount() {
    if (seatCount == null) {
      seatCount = (int) Streams.iteratorToStream(layoutIterator(0, LAYOUT.length(), false)).count();
    }

    return seatCount;
  }

  /**
   * Returns an iterator which iterate through all positions for a row in the layout map.
   *
   * @param row       the row
   * @param inclSpace if {@code true}, the iterator will emit space values (0)
   * @return the iterator
   */
  public static Iterator<Pair<Integer, Integer>> rowIterator(int row, boolean inclSpace) {
    return layoutIterator(row * LAYOUT.rowSize(), (row + 1) * LAYOUT.rowSize(), inclSpace);
  }

  /**
   * Returns an iterator which iterate through all positions of the layout map.
   *
   * @param start     the starting position (inclusive)
   * @param end       the ending position (exclusive)
   * @param inclSpace if {@code true}, the iterator will emit space values (0)
   * @return the iterator
   */
  public static Iterator<Pair<Integer, Integer>> layoutIterator(int start, int end, boolean inclSpace) {
    return new Iterator<Pair<Integer, Integer>>() {
      int idx = start;

      private int computeNext() {
        if (idx >= end) {
          return end;
        }

        int i = idx;
        for (; ; ) {
          i += Math.max(1, LAYOUT.at(i));

          if (i >= end) {
            return end;
          }

          if (inclSpace || LAYOUT.at(i) != 0) {
            return i;
          }
        }
      }

      @Override
      public boolean hasNext() {
        return computeNext() != end;
      }

      @Override
      public Pair<Integer, Integer> next() {
        Pair<Integer, Integer> result = new Pair<>(idx, LAYOUT.at(idx));
        idx = computeNext();
        return result;
      }
    };
  }

  /**
   * Returns the name of the seat at a position.
   *
   * @param seat the seat number
   * @return the seat name
   */
  public static String seatName(int seat) {
    Pair<Integer, Integer> rc = LAYOUT.indexToRC(seat);
    char letter = rowLetter(rc.first);
    int countFromBegin = ((int) Streams.iteratorToStream(layoutIterator(rc.first * LAYOUT.rowSize(), seat + 1, true))
      .filter(il -> il.second != 0)
      .count());

    return String.format("%c%d%s", letter, countFromBegin, LAYOUT.at(seat) > 1 ? "COUPLE" : "");
  }

  /**
   * Returns the letter representing a row.
   *
   * @param row the row number
   * @return the letter representing {@code row}
   */
  public static char rowLetter(int row) {
    return ((char) ((int) '@' + LAYOUT.rowCount() - row));
  }

  /**
   * Gets the cinema id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the cinema rank.
   *
   * @return the rank
   */
  public Rank getRank() {
    return rank;
  }

  /**
   * Gets the cinema name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * The ranks for a cinema.
   */
  enum Rank {
    /**
     * Platinum cinema.
     */
    PLATINUM,
    /**
     * Normal cinema.
     */
    NORMAL
  }
}
