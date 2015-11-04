package moblima.view;

import jni.Console;
import jni.Key;

public abstract class Component implements SimpleComponent {
	private final Component parent;
	private Component attachingChild = null;

	public Component(Component parent) {
		this.parent = parent;
	}

	protected final void cancel() {
		parent.onChildCanceled(this);
	}

	protected final void attach(Component child) {
		attachingChild = child;
	}

	public Component getParent() {
		return parent;
	}

	protected boolean doOnKey(char key) {
		return false;
	}

	public final boolean onKey(char key) {
		if (key == Key.ESC) {
			cancel();
			return true;
		}

		return doOnKey(key);
	}

	public void onChildCanceled(Component child) {
		if (child == attachingChild) {
			parent.onChildCanceled(this);
		} else {
			this.renderFocus();
		}
	}

	public void render() {
		if (attachingChild != null) {
			Console.clear();
			attachingChild.render();
		}
	}

	public void onFocus() {
		if (attachingChild != null) {
			attachingChild.setFocus();
		}
	}

	public final void renderFocus() {
		render();
		setFocus();
	}

	public final void setFocus() {
		App.inst.setFocus(this);
	}
}
