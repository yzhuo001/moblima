package moblima.view.menu;

import jni.Color;
import jni.Key;
import jni.TextColor;
import moblima.util.Pair;
import moblima.view.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MultipleMenu extends MenuStr<Consumer<Set<Pair<Integer, Object>>>> {
	private final Set<Integer> selected = new HashSet<>();

	public MultipleMenu(Component parent, Consumer<Set<Pair<Integer, Object>>> onAccept, String message, Object... options) {
		super(parent, onAccept, message, options);
	}

	public MultipleMenu(Component parent, Consumer<Set<Pair<Integer, Object>>> onAccept, Orientation orientation, String message, Object... options) {
		super(parent, onAccept, orientation, message, options);
	}

	public void select(int index) {
		selected.add(index);
	}

	@Override
	protected void renderFooter() {
		try (TextColor ignored = new TextColor(Color.CYAN)) {
			System.out.print("Selected");
		}

		if (orientation == Orientation.Horizontal) {
			selected.stream().forEach(val -> System.out.print(" " + options[val]));
			System.out.println();
		} else {
			System.out.println();
			selected.stream().forEach(val -> System.out.println(options[val]));
		}
	}

	@Override
	public boolean doOnKey(char c) {
		if (super.doOnKey(c)) {
			return true;
		}

		switch (c) {
			case Key.ENTER:
				selected.add(active);
				render();
				return true;

			case Key.DELETE:
				selected.remove(active);
				this.render();
				return true;

			case Key.CTRL_ENTER:
				if (selected.isEmpty()) {
					return false;
				}
				this.onAccept.accept(
					selected.stream()
						.map(sel -> new Pair<Integer, Object>(sel, options[sel]))
						.collect(Collectors.toSet()));
				return true;
		}

		return false;
	}
}
