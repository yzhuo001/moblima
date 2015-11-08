package moblima.view;

import jni.Color;
import moblima.model.Movie;

import java.util.stream.Collectors;

/**
 * Prints a {@link Movie} to a formatting string which can then be printed by {@link Printer}.
 */
public class MoviePrinter {
  private Movie movie;

  /**
   * Constructs the printer with a {@link Movie} to print.
   *
   * @param movie the movie to print
   */
  public MoviePrinter(Movie movie) {
    this.movie = movie;
  }

  /**
   * Gets the associated {@link Movie}.
   *
   * @return the movie
   */
  public Movie getMovie() {
    return movie;
  }

  /**
   *
   * Prints the {@code movie} to a string.
   *
   * @return the formatting string
   */
  @Override
  public String toString() {
    //first line: title | showing status | rating
    StringBuilder str = new StringBuilder();

    Color showingStatusBg = Color.GREEN;

    if (movie.getShowingStatus() != Movie.ShowingStatus.NOW_SHOWING) {
      showingStatusBg = Color.DARKGRAY;
    }

    str.append(String.format("{c,BLACK,%s;%s}", showingStatusBg, movie.getShowingStatus()));
    str.append(String.format("%3c%s", ' ', movie.getTitle().toUpperCase()));
    float rating = movie.getAverageRating();
    str.append(String.format("{c,MAGENTA;%3c%s}\n", ' ', Float.isNaN(rating) ? "No ratings yet" : String.format("Average rating: %.1f", rating)));

    str.append(movie.getClassification());
    str.append(String.format(" | %d min |", movie.getLength()));
    str.append(String.join(", ", movie.getGenres().stream().map(Object::toString).collect(Collectors.toList())));
    str.append("\n\n");

    str.append(movie.getSynopsis());
    str.append("\n");

    str.append(String.format("Director: %s\n%-7s: %s\n", movie.getDirector(), "Stars", String.join(", ", movie.getCasts())));
    str.append("------------------------------------------");

    return str.toString();
  }
}
