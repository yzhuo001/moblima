package sg.edu.ntu.sce.cx2002.group6.moblima.view.moviegoer;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.*;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Printer;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.ShowTimePrinter;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Util;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.LineEdit;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.Menu;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.SingleMenu;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code BookTicket} represents the ticket booking screen.
 */
public class BookTicket {
  /**
   * Displays the screen
   *
   * @param db    the database
   * @param movie the movie for which the booking is made
   */
  public static void exec(Database db, Movie movie) {
    List<ShowTimePrinter> printers = db
      .getShowTimeDb()
      .availableShowTimesForMovie(movie.getId())
      .map(st -> new ShowTimePrinter(st, db.getCineplexes(), db.getMovieDb()))
      .collect(Collectors.toList());

    if (printers.size() <= 0) {
      Util.pause("There are currently no available show times for this movie. Please check back letter", true);
      return;
    }

    SingleMenu<ShowTimePrinter> selectShowTime = new SingleMenu<>(
      Menu.Orientation.Vertical,
      String.format("Select a show time for %s: ", movie),
      printers
    );

    SingleMenu<String> confirm = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "Select an option: ",
      "Proceed to payment",
      "Cancel this booking"
    );

    final ShowTime[] showTime = new ShowTime[1];
    final List<Integer>[] seats = (List<Integer>[]) Array.newInstance(List.class, 1);
    final Customer[] customer = new Customer[1];

    //i: ignored, r: result. tooo lazy today :(
    selectShowTime.exec()
      .flatMap(r -> {
        showTime[0] = r.second.getShowTime();
        Console.clear();
        return new SeatLayout(
          db.getShowTimeDb().bookedSeats(showTime[0].getId())
        ).exec();
      })
      .flatMap(r -> {
        Console.clear();
        seats[0] = r;
        return inputCustomer(movie);
      })
      .flatMap(r -> {
        customer[0] = r;
        Console.clear();
        renderBookingSummary(showTime[0], db.getMovieDb(), movie.getGenres(), db.getCineplexes(), seats[0], db.getTicketPrice(), customer[0]);
        return confirm.exec();
      }).ifPresent(SingleMenu.map(
        () -> LineEdit.get("Enter your credit card number: ")
              .ifPresent(i -> completeBooking(db.getBookingDb(), showTime[0], seats[0], customer[0])),
        () -> {}
      ));
  }

  private static Optional<Customer> inputCustomer(Movie movie) {
    Customer customer = new Customer();

    return LineEdit.get("Enter your name: ")
      .flatMap(name -> {
        customer.setName(name);
        return LineEdit.getValid("Enter your phone number: ", customer::setPhone);
      })
      .flatMap(i -> LineEdit.getValid("Enter your email: ", customer::setEmail))
      .flatMap(i -> LineEdit.getValid("How old are you? ", line -> {
        try {
          int ageInt = Integer.parseInt(line);
          return customer.setAge(ageInt, movie.getClassification());
        } catch (NumberFormatException e) {
          return "Age should be an integer.";
        }
      }))
      .map(i -> customer);
  }

  private static void renderBookingSummary(ShowTime showTime, MovieDb movieDb, Set<Movie.Genre> genres, List<Cineplex> cineplexes,
                                           List<Integer> seats, TicketPrice ticketPrice,
                                           Customer customer) {
    Cinema cinema = showTime.getCinema(cineplexes);
    float totalPrice = seats.stream()
      .map(seat ->
        ticketPrice.calculate(seat, customer.getAge(), cinema.getRank(), genres, showTime.getStartTime()))
      .reduce(Float::sum).get();

    Console.clear();
    Printer.out("{c,GREEN;BOOKING SUMMARY}\n");
    System.out.printf("%-20s: %s\n", "Customer name", customer.getName());
    System.out.printf("%-20s: %s\n", "Show time", new ShowTimePrinter(showTime, cineplexes, movieDb));
    System.out.printf("%-20s: %s\n", "Seat(s)", seats.stream().map(Cinema::seatName).collect(Collectors.joining(", ")));
    Printer.outf(
      "%-20s: {c,RED;%.1f} SGD (GST included)\n",
      "Total price",
      totalPrice
    );
  }

  private static void completeBooking(BookingDb bookingDb, ShowTime showTime, List<Integer> seats, Customer customer) {
    System.out.print("Processing your payment");
    for (int i = 0; i < 10; ++i) {
      System.out.print('.');
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    Printer.out("\n{c,GREEN;Thank you for your payment. Your transaction has been completed!}\n");
    Booking booking = new Booking(showTime, seats, customer);
    bookingDb.add(booking);
    Util.pause(
      String.format(
        "Your transaction ID is {c,RED;%s}. Please keep this ID for future reference",
        booking.getTransactionId()
      )
    );
  }
}
