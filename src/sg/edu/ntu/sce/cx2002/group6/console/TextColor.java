package sg.edu.ntu.sce.cx2002.group6.console;

/**
 * {@code TextColor} is to be used with try-with-resource idiom to print a block of text with formatting.
 */
public class TextColor implements AutoCloseable {
  private int oldAttribute;

  /**
   * Sets the {@link Console} text attribute to print text om given {@code foreground} and default background.
   *
   * @param foreground the foreground
   */
  public TextColor(Color foreground) {
    this(foreground, Color.DEFAULT);
  }

  /**
   * Sets the {@link Console} text attribute to print text with given {@code foreground} and {@code background}.
   *
   * @param foreground the foreground
   * @param background the background
   */
  public TextColor(Color foreground, Color background) {
    oldAttribute = Console.getTextAttribute();
    int foregroundValue = foreground.value;
    if (foreground == Color.DEFAULT) {
      foregroundValue = oldAttribute & 0xf;
    }

    int backgroundValue = background.value;
    if (background == Color.DEFAULT) {
      backgroundValue = (oldAttribute & 0xf0) >> 4;
    }

    int newAttribute = foregroundValue | (backgroundValue << 4);
    Console.setTextAttribute(newAttribute);
  }

  /**
   * Wraps the call to {@code fn} in a try-with-resource {@code TextColor} block.
   *
   * @param foreground the foreground
   * @param background the background
   * @param fn         the fn
   */
  public static void wrap(Color foreground, Color background, Runnable fn) {
    try (TextColor ignored = new TextColor(foreground, background)) {
      fn.run();
    }
  }

  /**
   * Wraps the call to {@code fn} in a try-with-resource {@code TextColor} block.
   *
   * @param foreground the foreground
   * @param fn         the fn
   */
  public static void wrap(Color foreground, Runnable fn) {
    wrap(foreground, Color.DEFAULT, fn);
  }

  /**
   * Reset the {@link Console} text attribute to its state before this {@code TextColor} is constructed.
   */
  @Override
  public void close() {
    Console.setTextAttribute(oldAttribute);
  }
}
