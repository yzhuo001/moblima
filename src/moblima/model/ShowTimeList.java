package moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class ShowTimeList implements Serializable {
    private AutoIncrement showTimeId = new AutoIncrement();
    private List<ShowTime> showTimes = new ArrayList<>();
    private transient BookingList bookingList;

    public ShowTimeList(BookingList bookingList) {
        this.bookingList = bookingList;
    }

    public BookingList getBookingList() {
        return bookingList;
    }

    private int getIndexById(int showTimeId) {
        return Collections.binarySearch(showTimes, new ShowTime(showTimeId));
    }

    public Stream<ShowTime> getAvailableForMovie(int movieId) {
        return showTimes.stream()
                .filter(showTime -> showTime.getMovieId() == movieId
                        && bookingList.getAtShowTime(showTime.getId()).count() <= Cinema.SEAT_COUNT);
    }

    public Stream<ShowTime> getForMovie(int movieId) {
        return showTimes.stream().filter(showTime -> showTime.getMovieId() == movieId);
    }

    Stream<ShowTime> getAll() {
        return showTimes.stream();
    }


    public Optional<ShowTime> get(int showTimeId)
    {
        int index = getIndexById(showTimeId);
        return index < 0 ? Optional.<ShowTime>empty() : Optional.of(showTimes.get(index));
    }

    void remove(int id) {
        int index = getIndexById(id);
        if (index < 0) {
            return;
        }
        bookingList.removeAtShowTime(id); //remove all bookings at this show time first
        showTimes.remove(index);
    }

    ShowTime add(int movieId, int cinemaId, LocalDateTime startTime) {
        ShowTime newShowTime = new ShowTime(showTimeId.getNext(), movieId, cinemaId, startTime);
        showTimes.add(newShowTime);

        return newShowTime;
    }

    Stream<Integer> getBookedSeatsAt(int showTimeIndex) {
        ShowTime showTime = showTimes.get(showTimeIndex);
        return bookingList.getAtShowTime(showTime.getId()).map(Booking::getSeatId);
    }
}
