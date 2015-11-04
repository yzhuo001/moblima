package moblima.view;

import jni.Console;

public class Renderer {
	private Console.BufferInfo startPos;
	private Console.BufferInfo endPos;

	private void recordStartPos() {
		if (startPos != null) {
			Console.clear(startPos.cursorX, startPos.cursorY, -1, endPos.cursorY - startPos.cursorY + 1);
			Console.goToXY(startPos.cursorX, startPos.cursorY);
		} else {
			startPos = Console.getBufferInfo();
		}
	}

	public void run(Runnable r) {
		recordStartPos();
		r.run();
		endPos = Console.getBufferInfo();
	}
}
