package sg.edu.ntu.sce.cx2002.group6.moblima.view;

import sg.edu.ntu.sce.cx2002.group6.moblima.model.Cineplex;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.MovieDb;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.ShowTime;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * {@code ShowTimePrinter} prints a {@link ShowTime} to a formatting string which can then be printed by {@link Printer}.
 */
public class ShowTimePrinter {
  private String movie;
  private String cinema;
  private ShowTime showTime;

  /**
   * Constructs the printer will all the objects needed to print a {@link ShowTime}
   *
   * @param showTime   the show time
   * @param cineplexes the cineplexes
   * @param movieDb    the movie database
   */
  public ShowTimePrinter(ShowTime showTime, List<Cineplex> cineplexes, MovieDb movieDb) {
    this.showTime = showTime;
    movie = movieDb.get(showTime.getMovie()).toString();
    cinema = showTime.getCinema(cineplexes).toString();
  }

  /**
   * Gets the associated {@link ShowTime}
   *
   * @return the show time
   */
  public ShowTime getShowTime() {
    return showTime;
  }

  /**
   * Prints the {@code showTime} to a string.
   *
   * @return the formatting string
   */
  @Override
  public String toString() {
    LocalDateTime startTime = showTime.getStartTime();
    return String.format("%s %s %d-%s-%d %d:%d at %s",
      movie,
      startTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
      startTime.getDayOfMonth(),
      startTime.getMonth(),
      startTime.getYear(),
      startTime.getHour(),
      startTime.getMinute(),
      cinema
    );
  }
}
