package moblima.model;

import util.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a booking made by user.
 * Once created, this object cannot be modified.
 */
public class Booking implements Serializable {
  /**
   * The assigned ID.
   */
  private int showTime;
  /**
   * The chosen cinema.
   */
  private Pair<Integer, Integer> cineplexCinema;
  /**
   * The chosen seats.
   */
  private List<Integer> seats;
  /**
   * The customer's info.
   */
  private Customer customer;
  /**
   * The time at which the booking is made.
   */
  private LocalDateTime time;


  public Booking(ShowTime showTime, List<Integer> seats, Customer customer) {
    this.cineplexCinema = showTime.getCineplexCinema();
    this.seats = seats;
    this.showTime = showTime.getId();
    this.customer = customer;
    this.time = LocalDateTime.now();
  }

  /**
   * @return The customer's info
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * @return The assigned ID
   */
  public int getShowTime() {
    return showTime;
  }

  /**
   * @return The time at which the booking is made.
   */
  public LocalDateTime getTime() {
    return time;
  }

  /**
   * @return The transaction ID of the form XXXYYYYMMDDhhmm, where X: cinema ID, Y: year, m: month, d: date, h: hour, m: minute
   */
  public String getTransactionId() {
    return String.format("%s%04d%02d%02d%02d%02d%02d", cineplexCinema, time.getYear(), time.getMonthValue(), time.getDayOfMonth(),
      time.getHour(), time.getMinute(), time.getSecond());
  }

  /**
   * @return The chosen seats
   */
  public List<Integer> getSeats() {
    return seats;
  }
}