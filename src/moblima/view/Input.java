package moblima.view;

import jni.Color;
import jni.Console;
import jni.TextColor;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.*;

public class Input {
    public enum Orientation {
        Vertical,
        Horizontal
    }

    public static final Scanner sc = new Scanner(System.in);
    public static final char ESCAPE = 27;
    public static final char DEFAULT_INTERRUPT_CHAR = ESCAPE;

    public static Optional<String> line(String message) {
        return line(message, DEFAULT_INTERRUPT_CHAR);
    }

    public static Optional<String> line(String message, char interruptChar) {
        for (;;) {
            System.out.print(message);
            CompletableFuture<String> input = CompletableFuture.supplyAsync(sc::nextLine);
            boolean interrupted = false;
            while (!input.isDone() && !interrupted) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }

                if (Console.charPressed(interruptChar)) {
                    Console.putCharIn('\r');
                    interrupted = true;
                }
            }

            try {
                if (interrupted) {
                    if (option(Orientation.Horizontal, "Are you sure you want to cancel? ", "Yes", "No").orElse(0) == 0) {
                        return Optional.empty();
                    } else {
                        continue;
                    }
                }
                return Optional.of(input.get());
            } catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        }
    }

    public static Optional<Integer> integer(String message, int min, int max) {
        Optional<String> line = Input.line(message);
        return line.map(Integer::parseInt);
    }

    public static Optional<Integer> option(Orientation orientation, String message, String... options) {
        assert options.length > 0;
        System.out.print(message);
        if (orientation == Orientation.Vertical) {
            System.out.println();
        }
        Console.BufferInfo start = Console.getBufferInfo();
        int barWidth = Arrays.stream(options).map(String::length).max(Integer::compare).get();
        int active = 0;
        char prevKey = 72;
        char nextKey = 80;
        if (orientation == Orientation.Horizontal) {
            prevKey = 75;
            nextKey = 77;
        }
        for (;;) {
            Console.goToXY(start.cursorX, start.cursorY);
            for (int i = 0; i < options.length; ++i) {
                try (TextColor ignored = i == active ? new TextColor(Color.WHITE, Color.DARKBLUE) : null) {
                    System.out.print(options[i]);
                    for (int ii = options[i].length(); ii <= barWidth; ++ii) {
                        System.out.print(" ");
                    }
                }

                if (orientation == Orientation.Horizontal) {
                    System.out.print(" ");
                } else {
                    System.out.println();
                }
            }
            if (orientation == Orientation.Horizontal) {
                System.out.println();
            }

            boolean activeChanged = false;
            while (!activeChanged) {
                switch (Console.getChar()) {
                    case '\r': //enter
                        return Optional.of(active);

                    case DEFAULT_INTERRUPT_CHAR:
                        return Optional.empty();

                    case 224:
                        char key = Console.getChar();
                        if (key == prevKey) {
                            if (0 < active) {
                                --active;
                                activeChanged = true;
                            }
                        } else if (key == nextKey) {
                            if (active < options.length - 1) {
                                ++active;
                                activeChanged = true;
                            }
                        }
                }
            }
        }
    }
}
