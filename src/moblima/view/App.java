package moblima.view;

import jni.Console;

/**
 * Manages the program's main flow.
 * There should be ONLY ONE App instance during the program's runtime.
 * This instance can be accessed through #inst enum constant.
 */
public enum App {
	/**
	 * The single instance of App
	 */
	inst;

	/**
	 * The component having keyboard focus.
	 */
	private Component focus;

	private boolean running = true;

	/**
	 * Runs the keyboard loop.
	 */
	public void run() {
		for (; ; ) {
			char key = Console.getChar();

			//let all components handle key event, from child to parent
			for (Component com = focus; com != null && !com.onKey(key); com = com.getParent())
				;

			if (!running) {
				return;
			}
		}
	}

	/**
	 * @return The component having keyboard focus.
	 */
	public Component getFocus() {
		return focus;
	}


	/**
	 * @param focus The component will receive keyboard focus. If focus is null, the program will quit.
	 */
	public void setFocus(Component focus) {
		if (focus == null) {
			running = false;
		} else {
			this.focus = focus;
			focus.onFocus();
		}
	}
}
