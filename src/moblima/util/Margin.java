package moblima.util;

import jni.Console;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Margin implements AutoCloseable {
	PrintStream orgOut;
	private int topMargin;

	public Margin(int left, int top) {
		this.topMargin = top;
		orgOut = System.out;
		Console.goToXY(left, top);
		try {
			System.setOut(new PrintStream(System.out, true, "UTF-8") {
				@Override
				public void println() {
					Console.goToXY(left, ++topMargin);
				}

				@Override
				public void print(char s) {
					System.console().printf("%c", s);
				}

				@Override
				public void print(String s) {
					System.console().printf("%s", s);
				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		System.setOut(orgOut);
	}
}
