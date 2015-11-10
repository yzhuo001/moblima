package sg.edu.ntu.sce.cx2002.group6.moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@code BookingDb} is the database all of bookings made by movie goers.
 */
public class BookingDb implements Serializable {
  private List<Booking> bookings = new ArrayList<>();

  /**
   * Returns the number of seats booked in a stream of bookings
   *
   * @param stream the stream of bookings
   * @return a number of seats booked
   */
  public static int totalSeatCount(Stream<Booking> stream) {
    return stream.mapToInt(booking -> booking.getSeats().size()).sum();
  }

  /**
   * Returns all the bookings made for the given movie.
   *
   * @param movieId    the movie id
   * @param showTimeDb the show time database
   * @return a stream of all bookings made for the movie whose id is {@code movieId}
   */
  public Stream<Booking> bookingsForMovie(int movieId, ShowTimeDb showTimeDb) {
    return bookings.stream()
      .filter(booking ->
        showTimeDb.get(booking.getShowTime())
          .map(showTime -> showTime.getMovie() == movieId).orElse(false));
  }

  /**
   * Returns all the bookings made at the given show time.
   *
   * @param showTimeId the show time id
   * @return a stream of all bookings made at the show time whose id is {@code showTimeId}
   */
  public Stream<Booking> bookingsAtShowTime(int showTimeId) {
    return bookings.stream().filter(booking -> booking.getShowTime() == showTimeId);
  }

  /**
   * Returns all the bookings made by the user with the given email.
   *
   * @param email the email
   * @return a stream of all bookings made by the user with {@code email}
   */
  public Stream<Booking> bookingsForEmail(String email) {
    String lowerCaseEmail = email.toLowerCase();
    return bookings.stream().filter(booking -> booking.getCustomer().getEmail().equals(lowerCaseEmail));
  }

  /**
   * Removes all bookings at the given show time.
   *
   * @param showTimeId the show time id
   */
  public void removeBookingsAtShowTime(int showTimeId) {
    bookings.removeIf(booking -> booking.getShowTime() == showTimeId);
  }

  /**
   * Adds a new booking.
   *
   * @param booking the booking
   */
  public void add(Booking booking) {
    bookings.add(booking);
  }
}
