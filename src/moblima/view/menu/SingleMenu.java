package moblima.view.menu;

import jni.Key;
import moblima.view.Component;

import java.util.function.BiConsumer;

public class SingleMenu extends MenuStr<BiConsumer<Integer, Object>> {
	public SingleMenu(Component parent, BiConsumer<Integer, Object> onAccept, String message, Object... options) {
		super(parent, onAccept, message, options);
	}

	public SingleMenu(Component parent, BiConsumer<Integer, Object> onAccept, Orientation orientation, String message, Object... options) {
		super(parent, onAccept, orientation, message, options);
	}

	@Override
	protected void renderFooter() {

	}

	@Override
	public boolean doOnKey(char c) {
		if (super.doOnKey(c)) {
			return true;
		}

		if (c == Key.ENTER) {
			this.onAccept.accept(active, options[active]);
			return true;
		}

		return false;
	}
}