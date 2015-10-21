package jni;

public final class Console {
    static {
        System.loadLibrary("jni/console");
    }

    public static class BufferInfo {
        public short width;
        public short height;
        public short cursorX;
        public short cursorY;
    }

    public static native int getChar();
    public static native void goToXY(short x, short y);
    public static native void clear();
    public static native BufferInfo getBufferInfo();
    private static native int getTextAttribute();
    private static native void setTextAttribute(int attr);

    private static class Foreground {
        public static final int BLUE      = 0x0001; // text color contains blue.
        public static final int GREEN     = 0x0002; // text color contains green.
        public static final int RED       = 0x0004; // text color contains red.
        public static final int INTENSITY = 0x0008; // text color is intensified.
    }

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
        WHITE(Foreground.INTENSITY | Foreground.RED | Foreground.GREEN | Foreground.BLUE);

        int value;
        Color(int value) {
            this.value = value;
        }
    }

    public static final class TextAttribute implements AutoCloseable {
        private int oldAttribute;
        public TextAttribute(Color foreground, Color background) {
            oldAttribute = getTextAttribute();
            setTextAttribute(foreground.value | (background.value << 4));
        }

        @Override
        public void close() {
            setTextAttribute(oldAttribute);
        }
    }
}