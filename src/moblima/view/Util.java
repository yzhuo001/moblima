package moblima.view;

import jni.Color;
import jni.Console;
import jni.TextColor;

import java.util.function.Consumer;

public class Util {
	public static void printCenter(String str) {
		int indent = (Console.getBufferInfo().windowWidth() + str.length()) / 2;
		String format = String.format("%%%ds\n", indent);
		System.out.printf(format, str);
	}

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

	public static void pause(String message) {
		try (TextColor ignored = new TextColor(Color.GREEN)) {
			System.out.printf("%s%s", message, ". Press any key to continue...");
		}
		Console.getChar();
	}
}
