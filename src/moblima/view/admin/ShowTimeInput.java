package moblima.view.admin;

import moblima.model.*;
import moblima.view.DateTimeEdit;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;
import util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@code ShowTimeInput} provides view methods for editing a {@link ShowTime} object.
 */
class ShowTimeInput extends Input<ShowTime> {
  private final List<Cineplex> cineplexes;
  private final MovieDb movieDb;
  private final ShowTime showTime;

  /**
   * Constructs a {@code ShowTimeInput} with a {@link ShowTime} object and other objects needed in editing the {@code ShowTime}.
   *
   * @param showTime   the show time
   * @param cineplexes the list of cineplexes
   * @param movieDb    the movie database
   */
  public ShowTimeInput(ShowTime showTime, List<Cineplex> cineplexes, MovieDb movieDb) {
    this.showTime = showTime;
    this.cineplexes = cineplexes;
    this.movieDb = movieDb;
  }

  public ShowTime get() {
    return showTime;
  }

  /**
   * Displays a {@link SingleMenu} to edit the show time movie.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see ShowTime#getMovie()
   * @see ShowTime#setMovie(int)
   */
  @Order(1)
  public Optional<Object> movie() {
    SingleMenu<Movie> menu = new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Select a movie: ",
      movieDb.all().collect(Collectors.toList())
    );
    menu.setActive(showTime.getMovie());

    return menu.exec().map(obj(idxMoviePair -> showTime.setMovie(idxMoviePair.second.getId())));
  }

  private Optional<Object> cineplex() {
    SingleMenu<Cineplex> menu = new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "Select a cineplex: ",
      cineplexes.stream().collect(Collectors.toList())
    );
    menu.setActive(showTime.getCineplexCinema().first);

    return menu.exec().map(obj(idxCineplexPair -> showTime.getCineplexCinema().first = idxCineplexPair.second.getId()));
  }

  /**
   * Displays {@link SingleMenu}s to select the cineplex and the cinema for the show time.
   *
   * @return A non-empty {@link Optional} if accepted.
   * @see ShowTime#getCineplexCinema()
   * @see ShowTime#setCineplexCinema(Pair)
   */
  @Order(2)
  public Optional<Object> cinema() {
    return cineplex()
      .flatMap(i -> {
        SingleMenu<Cinema> menu = new SingleMenu<>(
          Menu.Orientation.Horizontal,
          "Select a cinema: ",
          cineplexes.get(showTime.getCineplexCinema().first).getCinemas().stream().collect(Collectors.toList())
        );
        menu.setActive(showTime.getCineplexCinema().second);
        return menu.exec();
      })
      .map(obj(idxCinemaPair -> showTime.getCineplexCinema().second = idxCinemaPair.second.getId()));
  }

  /**
   * Displays a {@link DateTimeEdit} to edit the show time starting time.
   *
   * @return A non-empty {@link Optional} if accepted.
   * * @see ShowTime#getStartTime()
   * @see ShowTime#setStartTime(LocalDateTime)
   */
  @Order(3)
  public Optional<Object> startTime() {
    return new DateTimeEdit(
      DateTimeEdit.Type.DATE_TIME,
      "Enter start time: ",
      showTime.getStartTime()
    ).exec().map(obj(showTime::setStartTime));
  }
}