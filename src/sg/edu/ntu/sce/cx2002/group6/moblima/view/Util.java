package sg.edu.ntu.sce.cx2002.group6.moblima.view;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.console.Color;

/**
 * A namespace for all view utilities.
 */
public class Util {
  /**
   * Gets the suffix of a day in month.
   *
   * @param day the day, ranging from 1 to 31
   * @return the suffix
   */
  public static String getDaySuffix(int day) {
    switch (day) {
      case 1:
      case 21:
      case 31:
        return "st";
      case 2:
      case 22:
        return "nd";
      case 3:
      case 23:
        return "rd";
      default:
        return "th";
    }
  }

  /**
   * Prints a successful message and pause the console screen.
   *
   * @param message the message
   */
  public static void pause(String message) {
    pause(message, false);
  }

  /**
   * Prints a message and pause the console screen.
   *
   * @param message the message
   * @param isError {@code true} if {@code message} is an error message,
   *                or {@code false} if {@code message} is a successful message
   */
  public static void pause(String message, boolean isError) {
    String color = (isError ? Color.RED : Color.GREEN).toString();
    Printer.outf("{c,%s;%s. Press any key to continue...}", color, message);
    Console.waitKey();
  }
}
