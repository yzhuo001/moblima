package sg.edu.ntu.sce.cx2002.group6.moblima.view;

import sg.edu.ntu.sce.cx2002.group6.moblima.model.Movie;
import sg.edu.ntu.sce.cx2002.group6.console.Color;
import sg.edu.ntu.sce.cx2002.group6.util.Pair;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Prints a {@link Movie} to a formatting string which can then be printed by {@link Printer}.
 */
public class MoviePrinter {
  private Movie movie;
  private Integer sales;

  /**
   * Constructs the printer with a {@link Movie} to print.
   *
   * @param movie the movie to print
   */
  public MoviePrinter(Movie movie) {
    this.movie = movie;
    sales = null;
  }

  /**
   * Constructs a new printer with a {@link Movie} and its sales number to print.
   *
   * @param movieSalesPair a pair of the movie and its sales number
   */
  public MoviePrinter(Pair<Movie, Integer> movieSalesPair) {
    movie = movieSalesPair.first;
    sales = movieSalesPair.second;
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
    str.append(String.format("   %s", movie.getTitle().toUpperCase()));
    Optional<Float> rating = movie.getAverageRating();
    str.append(String.format("{c,MAGENTA;   Average rating: %s}",
      rating.isPresent() ? String.format("%.1f", rating.get()) : "N/A"));

    if (sales != null) {
      str.append(String.format("    {c,CYAN; Sales: %d}", sales));
    }
    str.append('\n');

    str.append(movie.getClassification());
    str.append(String.format(" | %d min | ", movie.getLength()));
    str.append(String.join(", ", movie.getGenres().stream().map(Object::toString).collect(Collectors.toList())));
    str.append("\n\n");

    str.append(movie.getSynopsis());
    str.append("\n");

    str.append(String.format("Director: %s\n%-7s: %s\n", movie.getDirector(), "Stars", String.join(", ", movie.getCasts())));
    str.append("------------------------------------------");

    return str.toString();
  }
}
