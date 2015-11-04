package moblima.view.menu;

import jni.Console;
import jni.Key;
import jni.TextColor;
import moblima.util.Calc;
import moblima.view.Component;
import moblima.view.SimpleComponent;
import moblima.view.Util;

import java.util.function.BiConsumer;

public class PagedMenu extends Menu<SimpleComponent> {
	private final int itemsPerPage;
	private final int pageCount;
	private BiConsumer<Integer, SimpleComponent> onAccept;
	private int start;
	private int end;
	private int page = -1;

	public PagedMenu(Component parent, BiConsumer<Integer, SimpleComponent> onAccept, int itemsPerPage, SimpleComponent[] options) {
		super(parent, options, Key.UP, Key.DOWN);
		this.itemsPerPage = itemsPerPage;
		this.pageCount = options.length / itemsPerPage + 1;
		this.onAccept = onAccept;
		setPage(0);
	}

	public void render() {
		renderer.run(() -> {
			if (options.length <= 0) {
				Util.pause("No entries found");
				cancel();
				return;
			}

			Util.printCenter(String.format("Page %d/%d (entry %d/%d)", page + 1, pageCount, end, options.length));

			for (int i = start; i < end; ++i) {
				if (i == active) {
					Console.BufferInfo startPos = Console.getBufferInfo();
					options[i].render();
					Console.BufferInfo endPos = Console.getBufferInfo();

					try (TextColor ignored = i == active ? new TextColor(ACTIVE_FG, ACTIVE_BG) : null) {
						Console.clear(startPos.cursorX, startPos.cursorY, -1, endPos.cursorY - startPos.cursorY);
						options[i].render();
					}
				} else {
					options[i].render();
				}
				System.out.println("-----------------------");
			}
		});
	}

	@Override
	protected int clampActive(int active) {
		return Calc.clamp(active, start, end - 1);
	}

	private void setPage(int nextPage) {
		nextPage = Calc.clamp(nextPage, 0, pageCount - 1);
		this.page = nextPage;
		start = itemsPerPage * nextPage;
		active = start;
		end = Math.min(start + itemsPerPage, options.length);
	}

	@Override
	public boolean doOnKey(char c) {
		if (super.doOnKey(c)) {
			return true;
		}

		switch (c) {
			case Key.ENTER:
				this.onAccept.accept(active, options[active]);
				break;

			case Key.PAGE_DOWN:
				setPage(page + 1);
				this.render();
				break;

			case Key.PAGE_UP:
				setPage(page - 1);
				this.render();
				break;
		}

		return false;
	}
}
