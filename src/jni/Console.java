package jni;

import util.Pair;

/**
 * {@code Console} provides low-level console API through JNI calls.
 */
public final class Console {
  public static Runnable closeHandler = () -> {
  };

  static {
    System.loadLibrary("./console-" + System.getProperty("os.arch"));
    init();
  }

  /**
   * Clears the console out buffer.
   */
  public static void clear() {
    clear(0, 0, -1, -1);
  }

  /**
   * Clears the bottom-right rectangular area with top-left coordinate (x, y) of the console out buffer.
   *
   * @param x the x
   * @param y the y
   */
  public static void clear(int x, int y) {
    clear(x, y, -1, -1);
  }

  /**
   * Waits for a key event to be pressed.
   *
   * @return the pressed key
   */
  public static char waitKey() {
    char key = getCh();
    switch (key) {
      case 224:
        char extended = Console.waitKey();
        switch (extended) {
          case 72:
            key = Key.UP;
            break;

          case 73:
            key = Key.PAGE_UP;
            break;

          case 77:
            key = Key.RIGHT;
            break;

          case 80:
            key = Key.DOWN;
            break;

          case 75:
            key = Key.LEFT;
            break;

          case 81:
            key = Key.PAGE_DOWN;
            break;

          case 83:
            key = Key.DELETE;
            break;
        }
    }
    return key;
  }

  /**
   * Puts the character {@code ch} into the console in buffer.
   *
   * @param ch the character
   */
  public static void putCharIn(char ch) {
    putStrIn(String.valueOf(ch));
  }

  /**
   * Returns the (x, y) coordinate of the console cursor.
   *
   * @return the coordinate pair.
   */
  public static Pair<Integer, Integer> cursor() {
    BufferInfo bi = getBufferInfo();
    return new Pair<>(bi.cursorX, bi.cursorY);
  }

  /**
   * Determines if the key {@code ch} is pressed.
   *
   * @param key the key
   * @return true of {@code ch} is pressed, false otherwise.
   */
  public static native boolean isKeyDown(char key);

  private static native void init();

  /**
   * Put the string {@code str} into the console in buffer.
   *
   * @param str the string
   */
  public static native void putStrIn(String str);

  /**
   * Go to xy.
   *
   * @param x the x
   * @param y the y
   */
  public static native void goToXY(int x, int y);

  /**
   * Clear.
   *
   * @param x      the x
   * @param y      the y
   * @param width  the width
   * @param height the height
   */
  public static native void clear(int x, int y, int width, int height);

  private static native BufferInfo getBufferInfo();

  private static native char getCh();

  /**
   * Returns the text attribute of the console out buffer.
   *
   * @return the text attribute
   */
  static native int getTextAttribute();

  /**
   * Sets the text attribute of the console out buffer.
   *
   * @param attr the text attribute
   */
  static native void setTextAttribute(int attr);

  /**
   * Executes {@code handler} when the console window is closed.
   *
   * @param handler the handler
   */
  public static void onClose(Runnable handler) {
    closeHandler = handler;
  }

  /**
   * This method will be called by the JNI library when the console window is closed.
   */
  private static void handleClosing() {
    closeHandler.run();
  }

  private static class BufferInfo {
    public int width;
    public int height;
    public int cursorX;
    public int cursorY;
    public int windowTop;
    public int windowLeft;
    public int windowBottom;
    public int windowRight;
  }
}