package moblima.view;

import jni.Console;
import jni.Key;
import util.Pair;

import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * {@code LineEdit} is a one-line text-editor. Unlike a {@link Scanner}, a {@code LineEdit} can be interrupted by pressing
 * the {@link Component#REJECTING_KEY}.
 * A {@code LineEdit} is also configurable to validate user input and transform the entered string to an arbitrary type.
 */
public class LineEdit {
  private static Optional<String> nextLine() {
    Scanner sc = new Scanner(System.in);
    CompletableFuture<String> input = CompletableFuture.supplyAsync(sc::nextLine);
    boolean interrupted = false;
    while (!input.isDone() && !interrupted) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException ignore) {
      }

      if (Console.isKeyDown(Component.REJECTING_KEY)) {
        Console.putCharIn('\r'); //simulate ENTER
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

  /**
   * Gets the next string entered by the user, validates and transforms it to an arbitrary type.
   *
   * @param <R>          the desired type
   * @param label        the label displayed before the editor.
   * @param transform    a function taking the entered string, validating and transforming it to the desired type {@code R}.
   *                     It returns a pair {@code (transformed value, error string)}.
   *                     If the {@code error string} is not {@code null}, it will be displayed and the user will be asked to
   *                     enter the value again until {@code error string} is {@code null}.
   *                     The {@code transformed value} will be returned when the entered value is valid.
   * @param initialValue the initial value of the editor.
   * @return an {@link Optional} describing the transformed value or an empty {@code Optional} if the editor is rejected.
   */
  public static <R> Optional<R> get(String label, Function<String, Pair<R, String>> transform, String initialValue) {
    if (initialValue != null) {
      Console.putStrIn(initialValue);
    }

    for (; ; ) {
      System.out.print(label);
      Optional<String> line = nextLine();
      if (!line.isPresent()) { //rejected
        return Optional.empty();
      } else {
        String trimmed = line.get().trim();
        if (trimmed.isEmpty()) { //empty line, continue asking
          continue;
        }

        Pair<R, String> result = transform.apply(trimmed);
        if (result.second == null) { //no error
          return Optional.of(result.first);
        }

        Printer.outf("{c,RED;%s}\n", result.second);
      }
    }
  }

  /**
   * Overloaded version of {@link #get(String, Function, String)} with an empty initial value.
   */
  public static <R> Optional<R> get(String label, Function<String, Pair<R, String>> transform) {
    return get(label, transform, null);
  }

  /**
   * Overloaded version of {@link #getValid(String, Function, String)} with an empty initial value.
   */
  public static Optional<String> getValid(String label, Function<String, String> validate) {
    return getValid(label, validate, null);
  }

  /**
   * Gets the next validated string.
   *
   * @param label        the label displayed before the editor
   * @param validate     a function taking the entered string and returning null if the value is valid or an error string
   *                     explaining why the value is not acceptable.
   * @param initialValue the initial value of the editor
   * @return an {@link Optional} describing the validated string or an empty {@code Optional} if the editor is rejected.
   */
  public static Optional<String> getValid(String label, Function<String, String> validate, String initialValue) {
    return get(label, line -> new Pair<>(line, validate.apply(line)), initialValue);
  }

  /**
   * Overloaded version of {@link #get(String, String)} without an initial value.
   */
  public static Optional<String> get(String message) {
    return get(message, (String) null);
  }

  /**
   * Get the next entered string without any validation and transformation
   *
   * @param label        the label displayed before the editor
   * @param initialValue the initial value of the editor
   * @return an {@link Optional} describing the entered string or an empty {@code Optional} if the editor is rejected.
   */
  public static Optional<String> get(String label, String initialValue) {
    return get(label, line -> new Pair<>(line, null), initialValue);
  }

  /**
   * Gets the next entered password. All entered characters will be displayed as * for security purpose.
   *
   * @param label the label displayed before the editor
   * @return the entered password
   */
  public static Optional<String> getPassword(String label) {
    System.out.print(label);
    StringBuilder buffer = new StringBuilder();
    for (; ; ) {
      char c = Console.waitKey();
      if (c == Component.REJECTING_KEY) {
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
