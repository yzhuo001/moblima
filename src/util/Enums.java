package util;

/**
 * Utilities for enums.
 */
public class Enums {
  /**
   * Pretty prints an enum value. For example: AN_ENUM_VALUE -> An enum value.
   *
   * @param value the enum value
   * @return the pretty printed value
   */
  public static String prettyPrint(String value) {
    StringBuilder builder = new StringBuilder(value.toLowerCase().replace('_', ' '));
    builder.setCharAt(0, value.charAt(0));
    return builder.toString();
  }
}
