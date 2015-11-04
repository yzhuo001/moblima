package moblima.util;

import java.util.stream.Stream;

public class Streams {
	public static <T> String[] toString(Stream<T> stream) {
		return stream.map(T::toString).toArray(String[]::new);
	}

	public static <T> T at(Stream<T> stream, int index) {
		return stream.skip(index).findFirst().get();
	}
}
