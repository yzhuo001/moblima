package moblima.util;

import java.util.stream.IntStream;

public class Strings {
	public static String repeat(String c, int n) {
		return String.join("", IntStream.range(0, n).mapToObj(__ -> c).toArray(String[]::new));
	}

	public static String repeat(char c, int n) {
		return repeat(String.valueOf(c), n);
	}
}
