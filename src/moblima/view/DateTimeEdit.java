package moblima.view;

import jni.Key;
import moblima.view.menu.SingleMenu;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

public class DateTimeEdit extends Component {
	private final Consumer<LocalDateTime> onAccept;
	private final String message;
	private final boolean withTime;
	private LocalDateTime dateTime;
	private SingleMenu menu;
	private Renderer renderer = new Renderer();

	public DateTimeEdit(Component parent, Consumer<LocalDateTime> onAccept, String message) {
		this(parent, onAccept, message, true);
	}

	public DateTimeEdit(Component parent, Consumer<LocalDateTime> onAccept, String message, boolean withTime) {
		this(parent, LocalDateTime.now(), onAccept, message, withTime);
	}

	public DateTimeEdit(Component parent, LocalDateTime dateTime, Consumer<LocalDateTime> onAccept, String message) {
		this(parent, dateTime, onAccept, message, true);
	}

	public DateTimeEdit(Component parent, LocalDateTime dateTime, Consumer<LocalDateTime> onAccept, String message, boolean withTime) {
		super(parent);
		this.dateTime = dateTime;
		this.onAccept = onAccept;
		this.message = message;
		this.withTime = withTime;
		updateMenu();
	}

	private void updateMenu() {
		int active = menu == null ? 0 : menu.getActive();
		String day = String.format("%s%-10s", message, dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
		String month = String.format("%d%s", dateTime.getDayOfMonth(), Util.getDaySuffix(dateTime.getDayOfMonth()));
		String year = String.valueOf(dateTime.getYear());

		if (withTime) {
			menu = new SingleMenu(
				this,
				(i1, i2) -> onAccept.accept(dateTime),
				day, month, year,
				String.format("%d:", dateTime.getHour()),
				String.valueOf(dateTime.getMinute())
			);
		} else {
			menu = new SingleMenu(
				this,
				(i1, i2) -> onAccept.accept(dateTime),
				day, month, year
			);
		}

		menu.setActive(active);
		attach(menu);
	}

	@Override
	public void render() {
		renderer.run(menu::render);
	}

	@Override
	public boolean doOnKey(char key) {
		switch (key) {
			case Key.UP:
				switch (menu.getActive()) {
					case 0: //date
						dateTime = dateTime.minusDays(1);
						break;

					case 1: //month
						dateTime = dateTime.minusMonths(1);
						break;

					case 2: //year
						dateTime = dateTime.minusYears(1);
						break;

					case 3: //hour
						dateTime = dateTime.minusHours(1);
						break;

					case 4: //minute
						dateTime = dateTime.minusMinutes(1);
						break;

					default:
						assert false;
				}
				updateMenu();
				menu.setFocus();
				render();
				return true;

			case Key.DOWN:
				switch (menu.getActive()) {
					case 0: //day
						dateTime = dateTime.plusDays(1);
						break;

					case 1: //month
						dateTime = dateTime.plusMonths(1);
						break;

					case 2: //year
						dateTime = dateTime.plusYears(1);
						break;

					case 3: //hour
						dateTime = dateTime.plusHours(1);
						break;

					case 4: //minute
						dateTime = dateTime.plusMinutes(1);
						break;

					default:
						assert false;
				}
				updateMenu();
				menu.setFocus();
				render();
				return true;

			default:
				return false;
		}
	}
}
