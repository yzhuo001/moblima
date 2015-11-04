package moblima.view;


import jni.Color;
import jni.Console;
import jni.Key;
import jni.TextColor;
import moblima.model.Cinema;
import moblima.util.Margin;
import moblima.util.Strings;
import moblima.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class SeatLayout extends Component {
	private final IntConsumer onAccept;
	private final boolean[] isBooked = new boolean[Cinema.LAYOUT.getArr().length];
	private List<Integer> availablePositions = new ArrayList<>();

	private static final int CELL_WIDTH = 3;
	private int active = 0;

	public SeatLayout(Component parent, IntConsumer onAccept, IntStream bookedSeats) {
		super(parent);
		bookedSeats.forEach(i -> isBooked[i] = true);

		for (int i = 0; i < Cinema.LAYOUT.length(); ) {
			int len = Cinema.LAYOUT.at(i);
			if (len > 0) {
				if (!isBooked[i]) {
					availablePositions.add(i);
				}
				i += len;
			} else {
				++i;
			}
		}

		this.onAccept = onAccept;
	}

	@Override
	protected boolean doOnKey(char key) {
		switch (key) {
			case Key.LEFT:
				setActive(Math.floorMod(active - 1, availablePositions.size()));
				return true;

			case Key.RIGHT:
				setActive((active + 1) % availablePositions.size());
				return true;
		}

		return false;
	}

	private static int colToX(int col) {
		return (CELL_WIDTH + 2) * col + 5;
	}

	private static int rowToY(int row) {
		return 3 * row + 1;
	}

	private void setActive(int nextActive) {
		int pos = availablePositions.get(active);
		int nextPos = availablePositions.get(nextActive);
		Pair<Integer, Integer> rc = Cinema.LAYOUT.indexToRC(pos);
		Pair<Integer, Integer> nextRC = Cinema.LAYOUT.indexToRC(nextPos);

		//erase current active
		try (Margin margin = new Margin(colToX(rc.getSecond()), rowToY(rc.getFirst()))) {
			renderBox(pos);
		}

		//highlight next active
		try (TextColor tc = new TextColor(Color.WHITE, Color.RED);
		     Margin margin = new Margin(colToX(nextRC.getSecond()), rowToY(nextRC.getFirst()))
		) {
			renderBox(nextPos);
		}

		active = nextActive;
	}

	private static void renderTopRow(int... row) {
		renderRow('┌', __ -> '─', '┐', row);
	}

	private static void renderBottomRow(int... row) {
		renderRow('└', __ -> '─', '┘', row);
	}

	private static void renderMiddleRow(Function<Integer, Character> space, int... row) {
		renderRow('│', space , '│', row);
	}

	private static void renderRow(char begin, Function<Integer, Character> space, char end, int... row) {
		for (int i = 0; i < row.length; ) {
			int len = row[i];
			if (len == 0) {
				System.out.printf("%-" + (CELL_WIDTH + 2) + "c", ' ');
				++i;
			} else {
				final int finalI = i;
				System.out.print(begin);
				System.out.print(Strings.repeat(space.apply(finalI), (CELL_WIDTH + 2) * len - 2));
				System.out.print(end);
				i += len;
			}
		}
	}

	private static void renderBox(int len, char space) {
		renderTopRow(len);
		System.out.println();
		renderMiddleRow(__ -> space, len);
		System.out.println();
		renderBottomRow(len);
	}

	private void renderBox(int index) {
		renderBox(Cinema.LAYOUT.at(index), isBooked[index] ? 'x' : ' ');
	}

	@Override
	public void render() {
		Console.clear();
		System.out.println("Select your preferred seat: ");
		for (int row = 0, letter = (int)'@' + Cinema.LAYOUT.rowCount(); row < Cinema.LAYOUT.rowCount(); ++row, --letter) {
			int[] rowArr = Cinema.LAYOUT.getRow(row);

			System.out.printf("%5c", ' ');
			renderTopRow(rowArr);

			System.out.printf("\n%3c%2c", letter, ' ');
			final int finalRow = row;
			renderMiddleRow(col -> isBooked[Cinema.LAYOUT.rcToIndex(finalRow, col)] ? 'x' : ' ', rowArr);

			System.out.printf("\n%5c", ' ');
			renderBottomRow(rowArr);

			System.out.println();
		}
	}

	@Override
	public void onFocus() {
		setActive(availablePositions.get(0));
	}
}
