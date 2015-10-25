package moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a booking made by user.
 * Once created, this object cannot be modified.
 */
public class Booking implements Serializable {
    /**
     * The assigned ID.
     */
    private int showTimeId;
    /**
     * The chosen cinema.
     */
    private int cinemaId;
    /**
     * The chosen seat.
     */
    private int seatId;
    /**
     * The customer's info.
     */
    private MovieGoer customer;
    /**
     * The time at which the booking is made.
     */
    private LocalDateTime time;


    public Booking(ShowTime showTime, int seatId, MovieGoer customer) {
        this.seatId = seatId;
        this.cinemaId = showTime.getCinemaId();
        this.showTimeId = showTime.getId();
        this.customer = customer;
        this.time = LocalDateTime.now();
    }

    /**
     * @return The customer's info
     */
    public MovieGoer getCustomer() {
        return customer;
    }

    /**
     * @return The assigned ID
     */
    public int getShowtimeId() {
        return showTimeId;
    }

    /**
     * @return The time at which the booking is made.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * @return The transaction id of the form XXXYYYYMMDDhhmm, where X: cinema ID, Y: year, m: month, d: date, h: hour, m: minute
     */
    public String getTransactionId() {
        return String.format("%3d%4d%2d%2d%2d%2d%2d", cinemaId, time.getYear(), time.getMonthValue(), time.getDayOfMonth(),
                time.getHour(), time.getMinute(), time.getSecond());
    }

    /**
     * @return The chosen seat ID
     */
    public int getSeatId() {
        return seatId;
    }
}