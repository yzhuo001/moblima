package jni;

import java.util.Optional;

public class TextColor implements AutoCloseable {
    private int oldAttribute;

    public TextColor(Color foreground) {
        this(foreground, Color.DEFAULT);
    }

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

    @Override
    public void close() {
        Console.setTextAttribute(oldAttribute);
    }
}
