package moblima.view.admin;

import jni.Console;
import moblima.model.Cineplex;
import moblima.model.MovieDb;
import moblima.model.ShowTime;
import moblima.model.ShowTimeDb;
import moblima.view.Component;
import moblima.view.ShowTimePrinter;
import moblima.view.Util;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code ShowTimeManager} is the screen for managing the {@link ShowTimeDb}.
 */
public class ShowTimeManager {
  private ShowTimeDb showTimeDb;
  private List<Cineplex> cineplexes;
  private MovieDb movieDb;

  /**
   * Displays the screen
   *
   * @param showTimeDb the show time database
   * @param cineplexes the list of all cineplexes
   * @param movieDb    the movie database
   */
  public static void exec(ShowTimeDb showTimeDb, List<Cineplex> cineplexes, MovieDb movieDb) {
    ShowTimeManager self = new ShowTimeManager();
    self.showTimeDb = showTimeDb;
    self.cineplexes = cineplexes;
    self.movieDb = movieDb;
    self.execMain();
  }

  private void execMain() {
    SingleMenu<String> mainMenu = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "Select an action you want to perform: ",
      "Create new show time",
      "View show time list"
    );

    Component.loop(() -> {
      Console.clear();
      return mainMenu;
    }, SingleMenu.map(
      //create
      () -> new CreateEdit<>(new ShowTimeInput(new ShowTime(), cineplexes, movieDb)).create().ifPresent(showTime -> {
        if (!showTimeDb.add(showTime, movieDb)) {
          Util.pause("Clash detected. The given show time is not added", true);
        } else {
          Util.pause("Successfully added");
        }
      }),

      //list
      this::execShowTimeMenu
    ));
  }

  private void execShowTimeMenu() {
    Component.loop(() -> {
      List<ShowTimePrinter> printers = showTimeDb.all()
        .map(showTime -> new ShowTimePrinter(showTime, cineplexes, movieDb))
        .collect(Collectors.toList());

      if (printers.size() == 0) {
        Util.pause("No entries found", true);
        return null;
      }

      Console.clear();

      return new SingleMenu<>(
        Menu.Orientation.Vertical,
        "Select a show time to edit/delete: ",
        printers
      );
    }, intPrinterPair -> {
      ShowTimePrinter printer = intPrinterPair.second;
      ShowTime showTime = printer.getShowTime();
      new SingleMenu<>(
        Menu.Orientation.Vertical,
        String.format("You have selected {c,GREEN;%s}, What do you want to do?", printer),
        "Edit this show time",
        "Delete this show time"
      ).exec().ifPresent(SingleMenu.map(
        () -> new CreateEdit<>(new ShowTimeInput(showTime, cineplexes, movieDb)).edit(i -> printer.toString()),
        () -> new SingleMenu<>(
          Menu.Orientation.Vertical,
          String.format("Are you sure you want to delete %s? All associated bookings will also be deleted! ", printer),
          "Delete",
          "Go back"
        ).exec().ifPresent(intChoicePair -> {
          if (intChoicePair.first != 0) return;
          showTimeDb.remove(showTime.getId());
          Util.pause("Deleted");
        })
      ));
    });
  }
}
