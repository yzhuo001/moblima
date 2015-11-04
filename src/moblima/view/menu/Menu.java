package moblima.view.menu;

import jni.Color;
import moblima.view.Component;
import moblima.view.Renderer;

abstract public class Menu<OptionType> extends Component {
	protected final Color ACTIVE_BG = Color.DARKBLUE;
	protected final Color ACTIVE_FG = Color.WHITE;
	protected int active;
	protected Renderer renderer = new Renderer();
	protected OptionType[] options;
	private int prevKey;
	private int nextKey;

	public Menu(Component parent, OptionType[] options, int prevKey, int nextKey) {
		super(parent);
		this.prevKey = prevKey;
		this.nextKey = nextKey;
		this.options = options;
		active = 0;
	}

	abstract protected int clampActive(int active);

	@Override
	public boolean doOnKey(char c) {
		if (c == prevKey || c == nextKey) {
			int nextActive = clampActive(active + (c == prevKey ? -1 : 1));
			if (nextActive != active) {
				active = nextActive;
				render();
			}
		}

		return false;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public OptionType getActiveOption() {
		return options[active];
	}
}
