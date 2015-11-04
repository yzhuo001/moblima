package moblima.view;

import jni.Console;
import moblima.model.TicketPrice;
import moblima.view.menu.DeletableMenu;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;

import java.time.LocalDate;
import java.util.HashSet;

public class PriceManager extends Component {
	private final TicketPrice ticketPrice;

	public PriceManager(Component parent, TicketPrice ticketPrice) {
		super(parent);
		this.ticketPrice = ticketPrice;
		attach(new SingleMenu(
			this,
			this::onAccept,
			MenuStr.Orientation.Vertical,
			"Select a configuration you want to change: ",
			"Change base price",
			"Change price multiplier",
			"Add public holiday",
			"Remove public holiday"
		));
	}

	private void onAccept(int choice, Object ignored) {
		switch (choice) {
			case 0:
				askBasePrice();
				break;

			case 1:
				askMultiplier();
				break;

			case 2:
				askPublicHoliday();
				break;

			case 3:
				removePublicHoliday();
				break;
		}
	}

	private void askBasePrice() {
		new LineEdit(
			this,
			line -> {
				try {
					int price = Integer.parseInt(line);
					if (price > 0) {
						ticketPrice.setBasePrice(price);
						Util.pause("Saved");
						renderFocus();
						return null;
					}
				} catch (NumberFormatException ignored) {
				}
				return "Ticket price should be a positive integer. Try again!";
			},
			"Enter the new base price: ",
			String.valueOf(ticketPrice.getBasePrice())
		).renderFocus();
	}

	private void editMultiplier(int ignored, Object mul) {
		TicketPrice.Multiplier multiplier = (TicketPrice.Multiplier) mul;

		new LineEdit(
			this,
			line -> {
				try {
					float value = Float.parseFloat(line);
					if (value > 0) {
						ticketPrice.setMultiplier(multiplier, value);
						Util.pause("Saved");
						renderFocus();
						return null;
					}
				} catch (NumberFormatException ex) {
				}

				return "Price multiplier should be a positive number. Try again!";
			},
			String.format("Enter %s multiplier: ", multiplier),
			String.valueOf(ticketPrice.getMultiplier(multiplier))
		).renderFocus();
	}

	private void askMultiplier() {
		new SingleMenu(
			this,
			this::editMultiplier,
			"Select a multiplier to edit: ",
			(Object[]) TicketPrice.Multiplier.values()
		).renderFocus();
	}

	private void askPublicHoliday() {
		new DateTimeEdit(this, dateTime -> {
			ticketPrice.getHolidays().add(dateTime.toLocalDate());
			Util.pause("Added");
			renderFocus();
		}, "Enter new holiday: ", false).renderFocus();
	}

	private void removePublicHoliday() {
		Console.clear();
		HashSet<LocalDate> holidays = ticketPrice.getHolidays();

		if (holidays.isEmpty()) {
			Util.pause("No holidays have been added");
			this.renderFocus();
			return;
		}

		new DeletableMenu(
			this,
			(i1, i2) -> {
			},
			(ignored, holiday) -> {
				ticketPrice.getHolidays().remove(holiday);
				this.removePublicHoliday();
			},
			"Select a public holiday and press DEL to delete: ",
			(Object[]) ticketPrice.getHolidays().stream().toArray(LocalDate[]::new)
		).renderFocus();
	}
}