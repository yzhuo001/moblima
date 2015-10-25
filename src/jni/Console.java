package jni;

public final class Console {
    static {
        System.loadLibrary("console");
    }

    public static class BufferInfo {
        public int width;
        public int height;
        public int cursorX;
        public int cursorY;
    }
    public static void clear() {
        clear(0, 0, -1, -1);
    }

    public static void clear(int x, int y) {
        clear(x, y, -1, -1);
    }

    public static native boolean charPressed(char ch);
    public static native void putCharIn(char ch);
    public static native char getChar();
    public static native void goToXY(int x, int y);
    public static native void clear(int x, int y, int width, int height);
    public static native BufferInfo getBufferInfo();
    static native int getTextAttribute();
    static native void setTextAttribute(int attr);
}