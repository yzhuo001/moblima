package moblima.view.admin;

import jni.Console;
import moblima.model.Movie;
import moblima.model.MovieDb;
import moblima.view.Component;
import moblima.view.MoviePrinter;
import moblima.view.Util;
import moblima.view.menu.Menu;
import moblima.view.menu.PagedMenu;
import moblima.view.menu.SingleMenu;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The movie manager screen.
 */
public class MovieManager {
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
      "View and edit movie list"
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
      () -> {
        List<MoviePrinter> printers = movieDb.all().map(MoviePrinter::new).collect(Collectors.toList());

        if (printers.size() <= 0) {
          Util.pause("No entries found", true);
          return;
        }

        Console.clear();
        Component.loop(new PagedMenu<>(4, "List of all movies", printers), intPrinterPair -> {
          Movie movie = intPrinterPair.second.getMovie();
          new CreateEdit<>(new MovieInput(movie)).edit(Movie::toString);
        });
      }
    ));
  }
}
