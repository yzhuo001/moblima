package moblima;

import jni.Console;

public class Main {
    public static void main(String[] args) {
        System.out.print("---------- You have to pay: ");
        try (Console.TextAttribute ignored = new Console.TextAttribute(Console.Color.RED, Console.Color.YELLOW)) {
            System.out.print("$123");
        }
        System.out.print(" ----------");

        int ch = Console.getChar();
        System.out.printf("%c", ch);
    }
}
