package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BookingList implements Serializable {
    private List<Booking> bookings = new ArrayList<>();

    public Stream<Booking> getForMovie(int movieId, ShowTimeList showTimeList) {
        return bookings.stream()
                        .filter(booking ->
                            showTimeList.get(booking.getShowtimeId())
                                        .map(showTime -> showTime.getMovieId() == movieId).orElse(false));
    }

    public Stream<Booking> getAtShowTime(int showTimeId) {
        return bookings.stream().filter(booking -> booking.getShowtimeId() == showTimeId);
    }

    public Stream<Booking> getByEmail(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return bookings.stream().filter(booking -> booking.getCustomer().getEmail().equals(lowerCaseEmail));
    }

    public void removeAtShowTime(int showTimeId) {
        bookings.removeIf(booking -> booking.getShowtimeId() == showTimeId);
    }

    public void add(Booking booking) {
        bookings.add(booking);
    }
}
