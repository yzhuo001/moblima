package moblima.view.menu;

import jni.Key;
import jni.TextColor;
import moblima.util.Calc;
import moblima.view.Component;

import java.util.Arrays;

abstract public class MenuStr<AcceptType> extends Menu<Object> {
	protected final Orientation orientation;
	private final String format;
	protected AcceptType onAccept;
	private String message;

	public MenuStr(Component parent, AcceptType onAccept, String message, Object... options) {
		this(parent, onAccept, Orientation.Horizontal, message, options);
	}

	public MenuStr(Component parent, AcceptType onAccept, Orientation orientation, String message, Object... options) {
		super(parent, options,
			orientation == Orientation.Vertical ? Key.UP : Key.LEFT,
			orientation == Orientation.Vertical ? Key.DOWN : Key.RIGHT
		);
		this.format = orientation == Orientation.Horizontal
			? "%s"
			: "%-" + Arrays.stream(options).map(Object::toString).map(String::length).max(Integer::compare).get() + "s";
		this.orientation = orientation;
		this.message = message;
		this.options = options;
		this.onAccept = onAccept;
	}

	abstract protected void renderFooter();

	@Override
	public void render() {
		renderer.run(() -> {
			System.out.print(message);
			if (orientation == Orientation.Vertical) {
				System.out.println();
			}

			for (int i = 0; i < options.length; ++i) {
				try (TextColor ignored = i == active ? new TextColor(ACTIVE_FG, ACTIVE_BG) : null) {
					System.out.printf(format, options[i]);
				}

				if (orientation == Orientation.Vertical) {
					System.out.println();
				} else {
					System.out.print(" ");
				}
			}

			if (orientation == Orientation.Horizontal) {
				System.out.println();
			}

			renderFooter();
		});
	}

	@Override
	protected int clampActive(int active) {
		return Calc.clamp(active, 0, options.length - 1);
	}

	public enum Orientation {
		Horizontal,
		Vertical
	}
}
