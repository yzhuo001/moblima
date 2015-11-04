package moblima.view;

import jni.Console;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class LineEdit extends Component {
	private final Function<String, String> onAccept;
	private final String message;
	private String defaultValue;

	public LineEdit(Component parent, Function<String, String> onAccept, String message) {
		this(parent, onAccept, message, null);
	}

	public LineEdit(Component parent, Function<String, String> onAccept, String message, String defaultValue) {
		super(parent);
		this.onAccept = onAccept;
		this.message = message;
		this.defaultValue = defaultValue;
	}

	public LineEdit(Component parent, Consumer<String> onAccept, String message) {
		this(parent, onAccept, message, null);
	}

	public LineEdit(Component parent, Consumer<String> onAccept, String message, String defaultValue) {
		this(parent, line -> {
			onAccept.accept(line);
			return null;
		}, message, defaultValue);
	}

	public LineEdit(Component parent, Runnable onAccept, Object obj, String prop) {
		this(parent, onAccept, obj, prop, null);
	}

	public LineEdit(Component parent, Runnable onAccept, Object obj, String prop, String defaultValue) {
		this(parent, line -> {
			try {
				obj.getClass().getMethod("set" + prop, String.class).invoke(obj, line);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
				assert false;
			}
			onAccept.run();
		}, prop + ": ", defaultValue);
	}

	@Override
	public void onFocus() {
		if (defaultValue != null) {
			Console.putStrIn(defaultValue);
			defaultValue = null;
		}
		for (; ; ) {
			Optional<String> line = InterruptibleScanner.nextLine(message);
			if (!line.isPresent()) {
				cancel();
				return;
			} else {
				String error = onAccept.apply(line.get().trim());
				if (error == null) {
					return;
				} else {
					System.out.println(error);
				}
			}
		}
	}
}
