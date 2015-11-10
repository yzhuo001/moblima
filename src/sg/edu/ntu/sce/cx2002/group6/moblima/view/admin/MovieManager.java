package sg.edu.ntu.sce.cx2002.group6.moblima.view.admin;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.Movie;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.MovieDb;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Component;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.MoviePrinter;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Util;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.Menu;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.SingleMenu;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.PagedMenu;
import sg.edu.ntu.sce.cx2002.group6.util.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code MovieManager} is the screen for managing {@link MovieDb}.
 */
public class MovieManager {

  /**
   * Displays a list of movies from the given stream.
   *
   * @param movieStream a stream of movies
   * @param message     the message before the list
   * @param T           the type of {@code} movieStream}, determines where it is a stream of (movie, sales) pair
   *                    or a stream of {@link Movie}
   */
  public static void displayMovieList(Stream movieStream, String message, Class<?> T) {
    List<MoviePrinter> printers = (T == Pair.class)
      ? ((Stream<Pair<Movie, Integer>>) movieStream).map(MoviePrinter::new).collect(Collectors.toList())
      : ((Stream<Movie>) movieStream).map(MoviePrinter::new).collect(Collectors.toList());

    if (printers.size() <= 0) {
      Util.pause("No entries found", true);
      return;
    }

    PagedMenu<MoviePrinter> menu = new PagedMenu<>(5, message, printers);

    Component.loop(() -> { Console.clear(); return menu; }, intPrinterPair -> {
      Movie movie = intPrinterPair.second.getMovie();
      new CreateEdit<>(new MovieInput(movie)).edit(Movie::toString);
    });
  }

  /**
   * Displays the screen.
   *
   * @param movieDb the movie database
   */
  public static void exec(MovieDb movieDb) {
    SingleMenu<String> menu = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "Select an action: ",
      "Add a new movie",
      "View and edit movie list",
      "View top 5 movies by sales",
      "View top 5 movies by overall ratings"
    );
    Component.loop(() -> {
      Console.clear();
      return menu;
    }, SingleMenu.map(
      //create
      () -> new CreateEdit<>(new MovieInput(new Movie())).create().ifPresent(movie -> {
        movieDb.add(movie);
        Util.pause(String.format("%s has been added successfully", movie));
      }),

      //edit
      () -> displayMovieList(movieDb.all(), "List of all movies", Movie.class),
      () -> displayMovieList(movieDb.top5MoviesBySales(), "Top 5 movies by sales", Pair.class),
      () -> displayMovieList(movieDb.top5MoviesByRating(), "Top 5 movies by overall rating", Movie.class)
    ));
  }
}
