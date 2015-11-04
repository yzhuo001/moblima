package moblima.util;

public class Enums {
	public static String format(String value) {
		StringBuilder builder = new StringBuilder(value.toLowerCase().replace('_', ' '));
		builder.setCharAt(0, value.charAt(0));
		return builder.toString();
	}
}
