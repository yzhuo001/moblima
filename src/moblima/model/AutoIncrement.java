package moblima.model;

import java.io.Serializable;

/**
 * A counter which starts at 0 and is auto incremented each time #getNext is called
 */
class AutoIncrement implements Serializable {
  /**
   * The value to be emitted
   */
  private int next = 0;

  /**
   * Returns the next value.
   *
   * @return The next value
   */
  public int getNext() {
    return next++;
  }
}
