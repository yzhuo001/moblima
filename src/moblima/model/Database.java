package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Database implements Serializable {
	private Admin admin = new Admin("admin", "");
	private BookingDb bookingDb = new BookingDb();
	private ShowTimeDb showTimeDb = new ShowTimeDb(bookingDb);
	private MovieDb movieDb = new MovieDb(showTimeDb);
	private TicketPrice ticketPrice = new TicketPrice(20);
	private List<Cineplex> cineplexes = new ArrayList<>();

	public void wakeUp() {
		showTimeDb.bookingDb = bookingDb;
		movieDb.showTimeDb = showTimeDb;
	}

	public Admin getAdmin() {
		return admin;
	}

	public BookingDb getBookingDb() {
		return bookingDb;
	}

	public ShowTimeDb getShowTimeDb() {
		return showTimeDb;
	}

	public MovieDb getMovieDb() {
		return movieDb;
	}

	public TicketPrice getTicketPrice() {
		return ticketPrice;
	}

	public List<Cineplex> getCineplexes() {
		return cineplexes;
	}
}