package moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShowTimeDb implements Serializable {
	private AutoIncrement showTimeId = new AutoIncrement();
	private List<ShowTime> showTimes = new ArrayList<>();
	transient BookingDb bookingDb;

	public ShowTimeDb(BookingDb bookingDb) {
		this.bookingDb = bookingDb;
	}

	public BookingDb getBookingDb() {
		return bookingDb;
	}

	private int getIndexById(int showTimeId) {
		return Collections.binarySearch(showTimes, new ShowTime(showTimeId));
	}

	public Stream<ShowTime> getAvailableForMovie(int movieId) {
		return showTimes.stream()
			.filter(showTime -> showTime.getMovieId() == movieId
				&& bookingDb.getAtShowTime(showTime.getId()).count() <= Cinema.getSeatCount());
	}

	public Stream<ShowTime> getForMovie(int movieId) {
		return showTimes.stream().filter(showTime -> showTime.getMovieId() == movieId);
	}

	public Stream<ShowTime> getAll() {
		return showTimes.stream();
	}

	public Optional<ShowTime> get(int showTimeId) {
		int index = getIndexById(showTimeId);
		return index < 0 ? Optional.empty() : Optional.of(showTimes.get(index));
	}

	public ShowTime getAt(int index) {
		return showTimes.get(index);
	}

	public void remove(int id) {
		int index = getIndexById(id);
		if (index < 0) {
			return;
		}
		bookingDb.removeAtShowTime(id); //remove all bookings at this show time first
		showTimes.remove(index);
	}

	private static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
		return start1.isBefore(end2) && start2.isBefore(end1);
	}

	public boolean add(ShowTime showTime, MovieDb movieDb) {
		showTime.id = showTimeId.getNext();
		int length = movieDb.get(showTime.getMovieId()).get().getLength();
		LocalDateTime myEnd = showTime.getStartTime().plusMinutes(length);

		Predicate<ShowTime> filterOverlapping = other -> {
			if (other.getCinemaId() != showTime.getCinemaId() || other.getCineplexId() != showTime.getCineplexId()) {
				return false;
			}
			int otherLength = movieDb.get(other.getMovieId()).get().getLength();
			LocalDateTime otherEnd = other.getStartTime().plusMinutes(otherLength);
			return isOverlapping(showTime.getStartTime(), myEnd, other.getStartTime(), otherEnd);
		};

		boolean isClash = getAll().filter(filterOverlapping).findAny().isPresent();
		if (isClash) {
			return false;
		} else {
			showTimes.add(showTime);
			return true;
		}
	}

	public IntStream getBookedSeatsAt(int showTimeIndex) {
		ShowTime showTime = showTimes.get(showTimeIndex);
		return bookingDb.getAtShowTime(showTime.getId()).mapToInt(Booking::getSeatId);
	}
}
