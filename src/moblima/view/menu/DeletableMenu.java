package moblima.view.menu;

import jni.Console;
import jni.Key;
import moblima.view.Component;
import moblima.view.Util;

import java.util.function.BiConsumer;

public class DeletableMenu extends SingleMenu {
	private final BiConsumer<Integer, Object> onDelete;

	public DeletableMenu(Component parent, BiConsumer<Integer, Object> onAccept, BiConsumer<Integer, Object> onDelete,
	                     String message, Object... options) {
		super(parent, onAccept, message, options);
		this.onDelete = onDelete;
	}

	public DeletableMenu(Component parent, BiConsumer<Integer, Object> onAccept, BiConsumer<Integer, Object> onDelete,
	                     Orientation orientation, String message, Object... options) {
		super(parent, onAccept, orientation, message, options);
		this.onDelete = onDelete;
	}

	@Override
	public boolean doOnKey(char c) {
		if (super.doOnKey(c)) {
			return true;
		}

		if (c != Key.DELETE) {
			return false;
		}

		Console.clear();
		new SingleMenu(
			this,
			(choice, ignored) -> {
				if (choice == 1) {
					Util.pause("Deleted " + options[active]);
					onDelete.accept(active, options[active]);
				} else {
					renderFocus();
				}
			},
			String.format("Are you sure you want to remove %s? ", options[active]),
			"No",
			"Yes"
		).renderFocus();

		return true;
	}
}