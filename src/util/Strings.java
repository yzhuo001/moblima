package util;

import java.util.stream.IntStream;

/**
 * Utilities for strings
 */
public class Strings {
  /**
   * Repeat the given strings n times.
   *
   * @param c the string
   * @param n the number of times to repeat
   * @return the resultant string
   */
  public static String repeat(String c, int n) {
    return String.join("", IntStream.range(0, n).mapToObj(__ -> c).toArray(String[]::new));
  }

  /**
   * Repeat the given character n times.
   *
   * @param c the character
   * @param n the number of times to repeat
   * @return the resultant string
   */
  public static String repeat(char c, int n) {
    return repeat(String.valueOf(c), n);
  }
}
