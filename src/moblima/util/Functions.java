package moblima.util;

import java.util.function.Consumer;

public class Functions {
	public static Runnable chain(Runnable last, Consumer<Runnable>... fns) {
		Runnable chained = last;
		for (int i = fns.length - 1; i >= 0; --i) {
			final int finalI = i;
			final Runnable finalChained = chained;
			chained = () -> fns[finalI].accept(finalChained);
		}
		return chained;
	}
}
