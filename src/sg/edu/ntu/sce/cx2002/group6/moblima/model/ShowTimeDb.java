package sg.edu.ntu.sce.cx2002.group6.moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * {@code ShowTime} is the database for all added show times.
 */
public class ShowTimeDb implements Serializable {
  transient BookingDb bookingDb;
  private AutoIncrement showTimeId = new AutoIncrement();
  private List<ShowTime> showTimes = new ArrayList<>();

  /**
   * Constructs an empty show time database.
   *
   * @param bookingDb the booking database
   */
  public ShowTimeDb(BookingDb bookingDb) {
    this.bookingDb = bookingDb;
  }

  private static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
    return start1.isBefore(end2) && start2.isBefore(end1);
  }

  /**
   * Gets the booking database.
   *
   * @return the booking database
   */
  public BookingDb getBookingDb() {
    return bookingDb;
  }

  private int idToIdx(int showTimeId) {
    return Collections.binarySearch(showTimes, new ShowTime(showTimeId));
  }

  /**
   * Returns a stream of all available for booking show times for a given movie.
   *
   * @param movieId the movie id
   * @return the stream
   */
  public Stream<ShowTime> availableShowTimesForMovie(int movieId) {
    return showTimesForMovie(movieId).filter(showTime ->
      showTime.getStartTime().compareTo(LocalDateTime.now()) > 0 && //startTime > now
        BookingDb.totalSeatCount(bookingDb.bookingsAtShowTime(showTime.getId())) < Cinema.getSeatCount()
    );
  }

  /**
   * Returns a stream of all show times, available or not, for a given movie.
   *
   * @param movieId the movie id
   * @return the stream
   */
  public Stream<ShowTime> showTimesForMovie(int movieId) {
    return showTimes.stream().filter(showTime -> showTime.getMovie() == movieId);
  }

  /**
   * Return a stream of all show times.
   *
   * @return the stream
   */
  public Stream<ShowTime> all() {
    return showTimes.stream();
  }

  /**
   * Gets the show time associated with the given ID
   *
   * @param showTimeId the show time ID
   * @return an {@link Optional} object describing the show time, or an empty {@code Optional} if the show time is not found.
   */
  public Optional<ShowTime> get(int showTimeId) {
    int index = idToIdx(showTimeId);
    return index < 0 ? Optional.empty() : Optional.of(showTimes.get(index));
  }

  /**
   * Removes the show time associated with the given ID.
   *
   * @param id the show time ID
   */
  public void remove(int id) {
    int index = idToIdx(id);
    if (index < 0) {
      return;
    }
    bookingDb.removeBookingsAtShowTime(id); //remove all bookings at this show time first
    showTimes.remove(index);
  }

  /**
   * Adds a new show time to the database.
   *
   * @param showTime the show time
   * @param movieDb  the movie database
   * @return {@code true} if the show time is added successfully, or {@code false} if the given show time clashes with
   * another existing show time in the database.
   */
  public boolean add(ShowTime showTime, MovieDb movieDb) {
    showTime.id = showTimeId.getNext();
    int length = movieDb.get(showTime.getMovie()).getLength();
    LocalDateTime myEnd = showTime.getStartTime().plusMinutes(length);

    Predicate<ShowTime> filterOverlapping = other -> {
      if (!other.getCineplexCinema().equals(showTime.getCineplexCinema())) {
        return false;
      }
      int otherLength = movieDb.get(other.getMovie()).getLength();
      LocalDateTime otherEnd = other.getStartTime().plusMinutes(otherLength);
      return isOverlapping(showTime.getStartTime(), myEnd, other.getStartTime(), otherEnd);
    };

    if (all().anyMatch(filterOverlapping)) { //clash
      return false;
    } else {
      showTimes.add(showTime);
      return true;
    }
  }

  /**
   * Returns a stream of all booked seat numbers on the given show time.
   *
   * @param showTimeId the show time ID
   * @return the stream
   */
  public IntStream bookedSeats(int showTimeId) {
    ShowTime showTime = showTimes.get(idToIdx(showTimeId));
    return bookingDb.bookingsAtShowTime(showTime.getId()).map(Booking::getSeats)
      .flatMap(Collection::stream).mapToInt(Integer::valueOf);
  }
}
