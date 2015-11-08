package moblima.view.moviegoer;

import jni.Console;
import moblima.model.*;
import moblima.view.Component;
import moblima.view.LineEdit;
import moblima.view.MoviePrinter;
import moblima.view.Util;
import moblima.view.menu.Menu;
import moblima.view.menu.PagedMenu;
import moblima.view.menu.SingleMenu;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The home screen for movie goers
 */
public class MovieGoerSection {

  /**
   * Display the screen.
   *
   * @param db the database
   */
  public static void exec(Database db) {
    SingleMenu<String> menu = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "Select an action you want to perform:",
      "View list of movies",
      "Search for a movie",
      "View your booking history"
    );
    Component.loop(() -> {
      Console.clear();
      return menu;
    }, SingleMenu.map(
      () -> displayMovieList(db, "Movies available for booking", db.getMovieDb().moviesAvailableForBooking()),
      () -> search(db),
      () -> displayBookingHistory(db.getBookingDb(), db.getShowTimeDb(), db.getMovieDb(), db.getCineplexes())
    ));
  }

  private static void displayMovieList(Database db, String message, Stream<Movie> movieStream) {
    List<MoviePrinter> printers = movieStream.map(MoviePrinter::new).collect(Collectors.toList());

    if (printers.size() > 0) {
      Console.clear();
      new PagedMenu<>(
        4,
        message,
        printers
      ).exec().ifPresent(r -> displayMovieMenu(db, r.second.getMovie()));
    } else {
      Util.pause("No entries found");
    }
  }

  private static void search(Database db) {
    MovieDb movieDb = db.getMovieDb();
    new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Search by: ",
      "Title", "Cast", "Director"
    ).exec().ifPresent(SingleMenu.map(
      () -> LineEdit.get("Enter part of movie title: ").ifPresent(partialTitle -> displayMovieList(
        db,
        String.format("Movies containing '%s' in title", partialTitle),
        movieDb.moviesByTitle(partialTitle)
      )),

      () -> LineEdit.get("Enter a cast name: ").ifPresent(partialCast -> displayMovieList(
        db,
        String.format("Movies starring '%s'", partialCast),
        movieDb.moviesByCast(partialCast)
      )),

      () -> LineEdit.get("Enter a director name: ").ifPresent(partialDirector -> displayMovieList(
        db,
        String.format("Movies directed by '%s'", partialDirector),
        movieDb.moviesByDirector(partialDirector)
      ))
    ));
  }

  private static void displayMovieMenu(Database db, Movie movie) {
    new SingleMenu<>(
      Menu.Orientation.Vertical,
      String.format("You have selected %s. What do you want to do next?", movie),
      "Book a ticket",
      "Rate this movie",
      "See all reviews for this movie"
    ).exec().ifPresent(SingleMenu.map(
      () -> BookTicket.exec(db, movie),

      () -> rateMovie().ifPresent(review -> {
        movie.addReview(review);
        Util.pause("Your review has been recorded!");
        Console.clear();
      }),


      () -> new PagedMenu<>(
        5,
        "Reviews for " + movie,
        movie.getReviews().stream().map(ReviewPrinter::new).collect(Collectors.toList())
      ).exec()
    ));
  }

  private static void displayBookingHistory(BookingDb bookingDb, ShowTimeDb showTimeDb, MovieDb movieDb, List<Cineplex> cineplexes) {
    LineEdit.get("Enter your email address: ").ifPresent(email -> {
      List<BookingPrinter> printers = bookingDb.bookingsForEmail(email)
        .map(booking -> new BookingPrinter(booking, showTimeDb, cineplexes, movieDb))
        .collect(Collectors.toList());

      if (printers.size() == 0) {
        Util.pause("No entries found", true);
      } else {
        new PagedMenu<>(
          5,
          "Bookings under " + email,
          printers
        ).exec();
      }
    });
  }

  private static Optional<Review> rateMovie() {
    Review review = new Review();
    SingleMenu<Integer> ratingSelect = new SingleMenu<>(Menu.Orientation.Horizontal,
      "How many stars would you give this movie? ",
      IntStream.range(Review.MIN_RATING, Review.MAX_RATING + 1).boxed().collect(Collectors.toList())
    );

    return LineEdit.get("Enter your name: ")
      .flatMap(name -> {
        review.setUsername(name);
        return ratingSelect.exec();
      })
      .flatMap(r -> {
        review.setRating(r.second);
        return LineEdit.get("Enter a detailed review for this movie: ");
      })
      .map(text -> {
        review.setText(text);
        return review;
      });
  }
}
