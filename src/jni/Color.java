package jni;

/**
 * All available colors for {@link Console}.
 *
 * @see TextColor
 */
public enum Color {
  BLACK(0),
  DARKBLUE(Foreground.BLUE),
  DARKGREEN(Foreground.GREEN),
  DARKCYAN(Foreground.GREEN | Foreground.BLUE),
  DARKRED(Foreground.RED),
  DARKMAGENTA(Foreground.RED | Foreground.BLUE),
  DARKYELLOW(Foreground.RED | Foreground.GREEN),
  DARKGRAY(Foreground.RED | Foreground.GREEN | Foreground.BLUE),
  GRAY(Foreground.INTENSITY),
  BLUE(Foreground.INTENSITY | Foreground.BLUE),
  GREEN(Foreground.INTENSITY | Foreground.GREEN),
  CYAN(Foreground.INTENSITY | Foreground.GREEN | Foreground.BLUE),
  RED(Foreground.INTENSITY | Foreground.RED),
  MAGENTA(Foreground.INTENSITY | Foreground.RED | Foreground.BLUE),
  YELLOW(Foreground.INTENSITY | Foreground.RED | Foreground.GREEN),
  WHITE(Foreground.INTENSITY | Foreground.RED | Foreground.GREEN | Foreground.BLUE),
  DEFAULT(-1);

  int value;

  Color(int value) {
    this.value = value;
  }
}

class Foreground {
  public static final int BLUE = 0x0001;
  public static final int GREEN = 0x0002;
  public static final int RED = 0x0004;
  public static final int INTENSITY = 0x0008;
}
