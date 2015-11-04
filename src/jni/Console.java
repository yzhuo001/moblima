package jni;


import java.util.ArrayDeque;
import java.util.Queue;

public final class Console {
	static {
		System.loadLibrary("./libs/console");
	}

	public static class BufferInfo {
		public int width;
		public int height;
		public int cursorX;
		public int cursorY;
		public int windowTop;
		public int windowLeft;
		public int windowBottom;
		public int windowRight;

		public int windowWidth() {
			return windowRight - windowLeft;
		}
	}
	public static void clear() {
		clear(0, 0, -1, -1);
	}

	public static void clear(int x, int y) {
		clear(x, y, -1, -1);
	}

	public static char getChar() {
		char key = getCh();
		switch (key) {
			case 224:
				char extended = Console.getChar();
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

	public static void putCharIn(char ch) {
		System.out.println("ok!!");
		putStrIn(String.valueOf(ch));
	}

	public static native boolean charPressed(char ch);
	public static native void putStrIn(String str);
	public static native void goToXY(int x, int y);
	public static native void clear(int x, int y, int width, int height);
	public static native BufferInfo getBufferInfo();
	private static native char getCh();
	static native int getTextAttribute();
	static native void setTextAttribute(int attr);
}