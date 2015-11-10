package sg.edu.ntu.sce.cx2002.group6.moblima.view.moviegoer;


import sg.edu.ntu.sce.cx2002.group6.console.Color;
import sg.edu.ntu.sce.cx2002.group6.console.Key;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.Cinema;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Component;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Printer;
import sg.edu.ntu.sce.cx2002.group6.util.Pair;
import sg.edu.ntu.sce.cx2002.group6.util.Streams;
import sg.edu.ntu.sce.cx2002.group6.util.Strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * {@code SeatLayout} displays the seat layout for a cinema and allows the user to select seats by using the left/right arrow.
 * It returns a list of selected seats as a result of execution.
 */
public class SeatLayout extends Component<List<Integer>> {
  private static final int CELL_WIDTH = 3;
  private final boolean[] isBooked = new boolean[Cinema.LAYOUT.array().length];
  private final Set<Integer> selected = new HashSet<>();
  private final char BOOKED = '▒';
  private final char SELECTED = '√';
  private List<Integer> availableSeats = new ArrayList<>();
  private int active = 0;

  /**
   * Constructs a new seat layout with no parent.
   *
   * @param bookedSeats the stream of all booked seats
   */
  public SeatLayout(IntStream bookedSeats) {
    this(null, bookedSeats);
  }

  /**
   * Constructs a new seat layout with a parent.
   *
   * @param parent      the parent {@link Component}
   * @param bookedSeats the stream of all booked seats
   */
  public SeatLayout(Component parent, IntStream bookedSeats) {
    super(parent);
    bookedSeats.forEach(i -> isBooked[i] = true);

    //set available seats
    availableSeats = Streams.iteratorToStream(Cinema.layoutIterator(0, Cinema.LAYOUT.length(), false))
      .map(slPair -> slPair.first)
      .filter(seat -> !isBooked[seat])
      .collect(Collectors.toList());
  }

  private static String renderTopRowToStr(int... row) {
    return renderRowToStr('┌', __ -> '─', '┐', row);
  }

  private static String renderBottomRowToStr(int... row) {
    return renderRowToStr('└', __ -> '─', '┘', row);
  }

  private static String renderMiddleRowToStr(Function<Integer, Character> space, int... row) {
    return renderRowToStr('│', space, '│', row);
  }

  private static String renderRowToStr(char begin, Function<Integer, Character> space, char end, int... row) {
    StringBuilder rowStr = new StringBuilder();
    for (int i = 0; i < row.length; ) {
      int len = row[i];
      if (len == 0) {
        rowStr.append(String.format("%-" + (CELL_WIDTH + 2) + "c", ' '));
        ++i;
      } else {
        final int finalI = i;
        rowStr.append(begin);
        rowStr.append(Strings.repeat(space.apply(finalI), (CELL_WIDTH + 2) * len - 2));
        rowStr.append(end);
        i += len;
      }
    }

    return rowStr.toString();
  }

  private static String renderBoxToString(int len, char space) {
    return renderTopRowToStr(len) +
      '\n' +
      renderMiddleRowToStr(__ -> space, len) +
      '\n' +
      renderBottomRowToStr(len);
  }

  @Override
  protected boolean handleKey(char key) {
    switch (key) {
      case Key.LEFT:
        setActive(Math.floorMod(active - 1, availableSeats.size()));
        return true;

      case Key.RIGHT:
        setActive((active + 1) % availableSeats.size());
        return true;

      case ' ': //space
        int seat = availableSeats.get(active);
        if (selected.contains(seat)) {
          selected.remove(seat);
        } else {
          selected.add(seat);
        }
        setActive(active);
        return true;

      case Key.ENTER:
        if (!selected.isEmpty()) {
          setResult(new ArrayList<>(selected));
          return true;
        }
    }

    return false;
  }

  private void setActive(int nextActive) {
    int activeSeat = availableSeats.get(active);
    int nextActiveSeat = availableSeats.get(nextActive);

    //erase current active background
    renderBox(activeSeat, Color.DEFAULT);

    //highlight next active
    renderBox(nextActiveSeat, Color.RED);

    active = nextActive;
  }

  private char getSeatSpace(int seat) {
    return isBooked[seat] ? BOOKED : (selected.contains(seat) ? SELECTED : ' ');
  }

  private int colToX(int col) {
    return (CELL_WIDTH + 2) * col + 5 + renderer.getStart().first;
  }

  private int rowToY(int row) {
    return 3 * row + 4 + renderer.getStart().second;
  }

  private void renderBox(int seat, Color bg) {
    Pair<Integer, Integer> rc = Cinema.LAYOUT.indexToRC(seat);
    Printer.outf("{m,%d,%d;{c,DEFAULT,%s;%s}}", colToX(rc.second), rowToY(rc.first), bg,
      renderBoxToString(Cinema.LAYOUT.at(seat), getSeatSpace(seat)));
  }

  @Override
  public void doRender() {
    Printer.out("Select/deselect your preferred seats by pressing{c,WHITE,DARKBLUE; SPACE }, confirm by pressing{c,WHITE,DARKBLUE; ENTER }\n");
    Printer.outf("\n%40c{c,GREEN;S C R E E N}\n\n", ' ');
    for (int row = 0; row < Cinema.LAYOUT.rowCount(); ++row) {
      int[] rowArr = Cinema.LAYOUT.row(row);

      //top row
      System.out.printf("%5c%s", ' ', renderTopRowToStr(rowArr));

      //row letter & middle row
      char letter = Cinema.rowLetter(row);
      final int finalRow = row;
      System.out.printf("\n%3c%2c%s%3c%2c\n",
        letter,
        ' ',
        renderMiddleRowToStr(col -> getSeatSpace(Cinema.LAYOUT.rcToIndex(finalRow, col)), rowArr),
        letter,
        ' '
      );

      //bottom row
      System.out.printf("%s%s%s", renderBottomRowToStr(1), renderBottomRowToStr(rowArr), renderBottomRowToStr(1));

      System.out.println();
    }

    Printer.outf("\n%40c{c,MAGENTA;E N T R A N C E}\n\n", ' ');

    Printer.outf("{c,CYAN;LEGENDS}: %s: Booked %-10c %s: Selected",
      Strings.repeat(BOOKED, CELL_WIDTH),
      ' ',
      Strings.repeat(SELECTED, CELL_WIDTH)
    );
  }
}
