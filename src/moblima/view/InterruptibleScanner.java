package moblima.view;

import jni.Console;
import jni.Key;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class InterruptibleScanner {
	public static Optional<String> nextLine(String message) {
		return nextLine(message, Key.ESC);
	}

	public static Optional<Integer> nextInt(String message) {
		return nextInt(message, Key.ESC);
	}

	public static Optional<String> nextPassword(String message) {
		return nextPassword(message, Key.ESC);
	}

	public static Optional<String> nextLine(String message, char interruptChar) {
		System.out.print(message);

		Scanner sc = new Scanner(System.in);
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
				return Optional.empty();
			}
			return Optional.of(input.get());
		} catch (InterruptedException | ExecutionException e) {
			return Optional.empty();
		}
	}

	public static Optional<Integer> nextInt(String message, char interruptChar) {
		try {
			return nextLine(message, interruptChar).map(Integer::parseInt);
		} catch (NumberFormatException ignored) {
			return Optional.of(Integer.MIN_VALUE);
		}
	}

	public static Optional<String> nextPassword(String message, char interruptChar) {
		System.out.print(message);
		StringBuilder buffer = new StringBuilder();
		for (; ; ) {
			char c = Console.getChar();
			if (c == interruptChar) {
				return Optional.empty();
			} else if (c == Key.ENTER) {
				System.out.print("\n");
				return Optional.of(buffer.toString());
			}

			buffer.append(c);
			System.out.print('*');
		}
	}
}
