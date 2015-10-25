package moblima.model;

import java.io.Serializable;

public class Database implements Serializable {
    private Admin admin = new Admin("admin", "");
    private BookingList bookingList = new BookingList();
    private ShowTimeList showTimeList = new ShowTimeList(bookingList);
    private MovieList movieList = new MovieList(showTimeList);
    private TicketPrice ticketPrice = new TicketPrice(20, 1.3f);

    public Database() {}

    public Admin getAdmin() {
        return admin;
    }

    public BookingList getBookingList() {
        return bookingList;
    }

    public ShowTimeList getShowTimeList() {
        return showTimeList;
    }

    public MovieList getMovieList() {
        return movieList;
    }

    public TicketPrice getTicketPrice() {
        return ticketPrice;
    }
}