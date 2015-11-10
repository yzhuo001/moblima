package sg.edu.ntu.sce.cx2002.group6.moblima.view.moviegoer;

import sg.edu.ntu.sce.cx2002.group6.moblima.model.*;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Printer;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.ShowTimePrinter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code BookingPrinter} prints a {@link Booking} to a formatting string which can then be printed by {@link Printer}.
 */
public class BookingPrinter {
  private Booking booking;
  private ShowTimePrinter showTimePrinter;

  /**
   * Constructs the printer will all the objects needed to print a {@link Booking}
   *
   * @param booking    the booking
   * @param showTimeDb the show time data base
   * @param cineplexes the list of all cineplexes
   * @param movieDb    the movie database
   */
  public BookingPrinter(Booking booking, ShowTimeDb showTimeDb, List<Cineplex> cineplexes, MovieDb movieDb) {
    this.booking = booking;
    this.showTimePrinter = new ShowTimePrinter(showTimeDb.get(booking.getShowTime()).get(), cineplexes, movieDb);
  }

  /**
   * Prints the {@code booking} to a string.
   *
   * @return the formatting string
   */
  @Override
  public String toString() {
    return String.format("[%s] %s\nSeat(s): %s",
      booking.getTransactionId(), showTimePrinter,
      booking.getSeats().stream().map(Cinema::seatName).collect(Collectors.joining(", "))
    );
  }
}
