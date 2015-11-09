package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code Database} is the central database for MOBLIMA.
 */
public class Database implements Serializable {
  private Admin admin = new Admin("a", "");
  private BookingDb bookingDb = new BookingDb();
  private ShowTimeDb showTimeDb = new ShowTimeDb(bookingDb);
  private MovieDb movieDb = new MovieDb(showTimeDb);
  private TicketPrice ticketPrice = new TicketPrice(20);
  private List<Cineplex> cineplexes = new ArrayList<>();

  /**
   * The method is to be called when the {@code Database} is deserialized to initialize the missing objects due to
   * serialization
   */
  public void wakeUp() {
    showTimeDb.bookingDb = bookingDb;
    movieDb.showTimeDb = showTimeDb;
  }

  /**
   * Gets the admin.
   *
   * @return the admin
   */
  public Admin getAdmin() {
    return admin;
  }

  /**
   * Gets the booking database.
   *
   * @return the booking database
   */
  public BookingDb getBookingDb() {
    return bookingDb;
  }

  /**
   * Gets the show time database.
   *
   * @return the show time database
   */
  public ShowTimeDb getShowTimeDb() {
    return showTimeDb;
  }

  /**
   * Gets the movie database
   *
   * @return the movie database
   */
  public MovieDb getMovieDb() {
    return movieDb;
  }

  /**
   * Gets ticket price database.
   *
   * @return the ticket price database
   */
  public TicketPrice getTicketPrice() {
    return ticketPrice;
  }

  /**
   * Gets list of all cineplexes.
   *
   * @return list of all cineplexes
   */
  public List<Cineplex> getCineplexes() {
    return cineplexes;
  }
}