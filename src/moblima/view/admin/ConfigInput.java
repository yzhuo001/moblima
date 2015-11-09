package moblima.view.admin;

import jni.Console;
import moblima.model.TicketPrice;
import moblima.view.DateTimeEdit;
import moblima.view.LineEdit;
import moblima.view.Util;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;
import util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

/**
 * {@code ConfigInput} provides view methods for editing a {@link TicketPrice} object
 */
public class ConfigInput extends Input<TicketPrice> {
  private TicketPrice ticketPrice;

  /**
   * Constructs a {@code ConfigInput} with a {@link TicketPrice} object to edit.
   *
   * @param ticketPrice the ticket price
   */
  public ConfigInput(TicketPrice ticketPrice) {
    this.ticketPrice = ticketPrice;
  }

  /**
   * Displays a {@link LineEdit} to edit the base price.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see TicketPrice#setBasePrice(int)
   * @see TicketPrice#getBasePrice()
   */
  @Order(1)
  public Optional<Object> changeBasePrice() {
    return LineEdit.getValid(
      "Enter the new base price: ",
      line -> {
        try {
          int price = Integer.parseInt(line);
          return ticketPrice.setBasePrice(price);
        } catch (NumberFormatException e) {
          return "Ticket price should be an integer.";
        }
      },
      String.valueOf(ticketPrice.getBasePrice())
    ).map(ok);
  }

  /**
   * Displays a {@link SingleMenu} of all multipliers then a {@link LineEdit} to change the selected multiplier value.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see TicketPrice#getMultiplier(TicketPrice.Multiplier)
   * @see TicketPrice#setMultiplier(TicketPrice.Multiplier, float)
   */
  @Order(2)
  public Optional<Object> editMultiplier() {
    return new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Select a multiplier to edit: ",
      TicketPrice.Multiplier.values()
    ).exec().map(this::editMultiplierValue);
  }

  private Optional<Object> editMultiplierValue(Pair<Integer, TicketPrice.Multiplier> intMultiplierPair) {
    TicketPrice.Multiplier multiplier = intMultiplierPair.second;

    return LineEdit.getValid(
      String.format("Enter %s multiplier: ", multiplier),
      line -> {
        try {
          float value = Float.parseFloat(line);
          return ticketPrice.setMultiplier(multiplier, value);
        } catch (NumberFormatException ex) {
          return "Price multiplier should be a number.";
        }
      },
      String.valueOf(ticketPrice.getMultiplier(multiplier))
    ).map(ok);
  }

  /**
   * Displays a {@link DateTimeEdit} to add a new holiday.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see TicketPrice#getHolidays()
   */
  @Order(3)
  public Optional<Object> addPublicHoliday() {
    return new DateTimeEdit(DateTimeEdit.Type.DATE_ONLY, "Enter new holiday: ", null)
      .exec()
      .map(obj(dateTime -> ticketPrice.getHolidays().add(dateTime.toLocalDate())));
  }

  /**
   * Displays a {@link SingleMenu} of all added holidays to select a holiday to delete.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see TicketPrice#getHolidays()
   */
  @Order(4)
  public Optional<Object> deletePublicHoliday() {
    Console.clear();
    HashSet<LocalDate> holidays = ticketPrice.getHolidays();
    if (holidays.size() <= 0) {
      Util.pause("No holidays added");
      return Optional.empty();
    } else {
      return new SingleMenu<>(
        Menu.Orientation.Horizontal,
        "Select a holiday to delete: ",
        new ArrayList<>(holidays)
      ).exec().map(obj(intHolidayPair -> holidays.remove(intHolidayPair.second)));
    }
  }

  @Override
  public TicketPrice get() {
    return ticketPrice;
  }
}