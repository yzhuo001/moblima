package moblima.model;

import util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@code MovieDb} is the database for all added movies.
 */
public class MovieDb implements Serializable {
  transient ShowTimeDb showTimeDb;
  private AutoIncrement movieId = new AutoIncrement();
  private List<Movie> movies = new ArrayList<>();

  /**
   * Constructs an empty movie database.
   *
   * @param showTimeDb the show time database
   */
  public MovieDb(ShowTimeDb showTimeDb) {
    this.showTimeDb = showTimeDb;
  }

  /**
   * Adds a new movie to the list.
   *
   * @param movie the movie
   * @return ID of the newly added movie.
   */
  public int add(Movie movie) {
    movie.id = movieId.getNext();
    movies.add(movie);
    return movie.id;
  }

  /**
   * Returns a stream of top 5 movies by rating.
   *
   * @return the stream
   */
  public Stream<Movie> top5MoviesByRating() {
    return movies.stream()
      .sorted((o1, o2) -> Float.compare(o2.getAverageRating().orElse(-1.f), o1.getAverageRating().orElse(-1.f))) //descending
      .limit(5);
  }

  /**
   * Returns a stream of top 5 movies by sales.
   *
   * @return the stream of pair (movie, sales)
   */
  public Stream<Pair<Movie, Integer>> top5MoviesBySales() {
    return movies.stream()
      .map(movie -> new Pair<>(movie, BookingDb.totalSeatCount(showTimeDb.getBookingDb().bookingsForMovie(movie.getId(), showTimeDb))))
      .sorted((o1, o2) -> Integer.compare(o2.second, o1.second)) //descending
      .limit(5);
  }

  /**
   * Removes the movie associated with the given ID.
   *
   * @param id the id
   */
  public void remove(int id) {
    //remove all show times for this movie
    showTimeDb.showTimesForMovie(id).map(ShowTime::getId).forEach(showTimeDb::remove);

    movies.remove(id);
  }

  /**
   * Returns the movie associated with the given ID.
   *
   * @param id the id
   * @return the movie
   */
  public Movie get(int id) {
    return movies.get(id);
  }

  /**
   * Returns a stream of all movies.
   *
   * @return the stream
   */
  public Stream<Movie> all() {
    return movies.stream();
  }

  /**
   * Returns a stream of movies whose title contains the given text
   *
   * @param partialTitle a part of the movie title
   * @return the stream
   */
  public Stream<Movie> moviesByTitle(String partialTitle) {
    String lowered = partialTitle.toLowerCase();
    return all().filter(movie -> movie.getTitle().toLowerCase().contains(lowered));
  }

  /**
   * Returns a stream of movies starring a cast whose name contains the given text
   *
   * @param partialCast a part of the cast name
   * @return the stream
   */
  public Stream<Movie> moviesByCast(String partialCast) {
    String lowered = partialCast.toLowerCase();
    return all().filter(movie -> movie.getCasts().toLowerCase().contains(lowered));
  }

  /**
   * Returns a stream of movies directed by a director whose name contains the given text
   *
   * @param partialDirector a part of the director name
   * @return the stream
   */
  public Stream<Movie> moviesByDirector(String partialDirector) {
    String lowered = partialDirector.toLowerCase();
    return all().filter(movie -> movie.getDirector().toLowerCase().contains(lowered));
  }

}
