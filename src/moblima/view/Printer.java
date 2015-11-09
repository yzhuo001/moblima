package moblima.view;

import jni.Color;
import jni.Console;
import jni.TextColor;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@code Printer} supports printing text on the {@link Console} screen with color and margin.
 */
public class Printer {
  static private final Pattern FORMAT = Pattern.compile("\\{(c|m),(.+?);(.+)}", Pattern.MULTILINE | Pattern.DOTALL);

  /**
   * Prints a formatted string with margin and color support.
   * This is a convenient method and equivalent to {@code out(String.format(fmt, args));}
   *
   * @see #out(String)
   */
  public static void outf(String fmt, Object... args) {
    out(String.format(fmt, args));
  }

  /**
   * Prints the string {@code s} with margin and color support.
   * The syntax for color is {c,[foreground][,background];text} where foreground and background are members of
   * the {@link Color} enum. If any of these values is omitted, it is defaulted to {@code Color.DEFAULT}.
   * Example:
   * - {@code Printer.out("Hello, {c,RED,BLUE;Alice}");} prints {@code Alice} in red on blue background.
   * - {@code Printer.out("Hello, {c,RED;Alice}");} prints {@code Alice} in red on default background.
   * <p>
   * The syntax for margin is {@code {m,x,y;text}} where {@code x} and {@code y} are the left and top margins.
   *
   * @param s the string with formatting to be printed
   */
  public static void out(String s) {
    new Impl(s).printStr(null, false);
  }

  private static class Impl {
    private String s;
    private int i;

    public Impl(String s) {
      this.s = s;
    }

    private char get() {
      assert i > -1 && i < s.length();
      return s.charAt(i++);
    }

    private List<String> getArgs() {
      char c = get();
      assert c == ',';
      List<String> args = new ArrayList<>();
      StringBuilder arg = new StringBuilder();
      for (; ; ) {
        c = get();
        switch (c) {
          case ';':
            args.add(arg.toString());
            return args;

          case ',':
            args.add(arg.toString());
            arg.setLength(0);
            break;

          default:
            arg.append(c);
        }
      }
    }

    private void printFmt(Pair<Integer, Integer> margin) {
      switch (get()) {
        case 'c': {
          List<String> args = getArgs();
          assert 1 <= args.size() && args.size() <= 2;
          Color fg = args.get(0).isEmpty() ? Color.DEFAULT : Color.valueOf(args.get(0));
          Color bg = args.size() == 2 ? Color.valueOf(args.get(1)) : Color.DEFAULT;
          TextColor.wrap(fg, bg, () -> printStr(margin, true));
          break;
        }

        case 'm': {
          List<String> args = getArgs();
          assert args.size() == 2;
          Pair<Integer, Integer> coord = new Pair<>(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
          Console.goToXY(coord.first, coord.second);
          printStr(coord, true);
          break;
        }

        default:
          assert false;
      }
    }

    public void printStr(Pair<Integer, Integer> margin, boolean inFmt) {
      StringBuilder builder = new StringBuilder();
      Runnable flush = () -> {
        System.out.print(builder);
        builder.setLength(0);
      };

      for (; i < s.length(); ++i) {
        char c = s.charAt(i);
        switch (c) {
          case '\n':
            flush.run();
            if (margin != null) {
              Console.goToXY(margin.first, ++margin.second);
            } else {
              System.out.println();
            }
            break;

          case '{':
            flush.run();
            ++i;
            printFmt(margin);
            break;

          case '}':
            if (inFmt) {
              flush.run();
              return;
            }

          default: //fall thru
            builder.append(c);
        }
      }

      flush.run();
    }
  }
}
