package moblima.view.admin;

import moblima.model.Movie;
import moblima.view.LineEdit;
import moblima.view.menu.Menu;
import moblima.view.menu.MultipleMenu;
import moblima.view.menu.SingleMenu;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides view methods for editing a {@link Movie} object
 */
public class MovieInput extends Input<Movie> {
  private Movie movie;

  /**
   * Constructs a {@code MovieInput} with a {@link Movie} object to edit.
   *
   * @param movie the movie
   */
  public MovieInput(Movie movie) {
    this.movie = movie;
  }

  /**
   * Displays a {@link LineEdit} to edit the movie title.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see Movie#getTitle()
   * @see Movie#setTitle(String)
   */
  @Order(1)
  public Optional<Object> title() {
    return LineEdit.get("Title: ", movie.getTitle()).map(obj(movie::setTitle));
  }

  /**
   * Displays a {@link MultipleMenu} to edit the movie genres.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see moblima.model.Movie.Genre
   */
  @Order(2)
  public Optional<Object> genres() {
    MultipleMenu<Movie.Genre> menu = new MultipleMenu<>(
      Menu.Orientation.Horizontal,
      "Select one or more genres: ",
      Movie.Genre.values()
    );

    movie.getGenres().stream().forEach(genre -> menu.select(genre.ordinal()));

    return menu.exec().map(obj(r -> movie.setGenres(r.stream().map(p -> p.second).collect(Collectors.<Movie.Genre>toSet()))));
  }

  /**
   * Displays a {@link SingleMenu} to edit the movie classification.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see moblima.model.Movie.Classification
   */
  @Order(3)
  public Optional<Object> classification() {
    SingleMenu<Movie.Classification> menu = new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Select movie classification: ",
      Movie.Classification.values()
    );
    menu.setActive(movie.getClassification().ordinal());

    return menu.exec().map(obj(r -> movie.setClassification(r.second)));
  }

  /**
   * Displays a {@link SingleMenu} to edit the movie showing status.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see moblima.model.Movie.ShowingStatus
   */
  @Order(4)
  public Optional<Object> showingStatus() {
    SingleMenu<Movie.ShowingStatus> menu = new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Select showing status: ",
      Movie.ShowingStatus.values()
    );
    menu.setActive(movie.getShowingStatus().ordinal());
    return menu.exec().map(obj(r -> movie.setShowingStatus(r.second)));
  }

  /**
   * Displays a {@link LineEdit} to edit the movie length.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see Movie#getLength()
   * @see Movie#setLength(int)
   */
  @Order(5)
  public Optional<Object> length() {
    return LineEdit.getValid(
      "Length in minutes: ",
      line -> {
        try {
          int length = Integer.parseInt(line);
          return movie.setLength(length);
        } catch (NumberFormatException e) {
          return "Movie length should be an integer!";
        }
      },
      String.valueOf(movie.getLength())
    ).map(ok);
  }

  /**
   * Displays a {@link LineEdit} to edit the movie casts.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see Movie#getCasts()
   * @see Movie#setCasts(String)
   */
  @Order(6)
  public Optional<Object> casts() {
    return LineEdit.getValid(
      "Enter 2 or more casts, separated by ,: ",
      movie::setCasts,
      movie.getCasts()
    ).map(ok);
  }

  /**
   * Displays a {@link LineEdit} to edit the movie director.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see Movie#getDirector()
   * @see Movie#setDirector(String)
   */
  @Order(7)
  public Optional<Object> director() {
    return LineEdit.get("Director: ", movie.getDirector()).map(obj(movie::setDirector));
  }

  /**
   * Displays a {@link LineEdit} to edit the movie synopsis.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see Movie#getSynopsis()
   * @see Movie#setSynopsis(String)
   */
  @Order(8)
  public Optional<Object> synopsis() {
    return LineEdit.get("Synopsis: ", movie.getSynopsis()).map(obj(movie::setSynopsis));
  }

  @Override
  public Movie get() {
    return movie;
  }
}
