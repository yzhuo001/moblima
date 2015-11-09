package moblima.view.admin;

import jni.Console;
import moblima.model.Database;
import moblima.view.Component;
import moblima.view.LineEdit;
import moblima.view.Printer;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;

import java.util.Optional;

/**
 * {@code AdminSection} authenticates the administrator and displays a list of sub-sections for the administrator to manage.
 */
public class AdminSection {
  /**
   * Displays the screen.
   *
   * @param db the application database
   */
  public static void exec(Database db) {
    Console.clear();
    System.out.println("Please enter your username and password.");

    for (; ; ) {
      Optional<Boolean> ok = LineEdit.get("Username: ").flatMap(username ->
        LineEdit.getPassword("Password: ").map(password -> db.getAdmin().verify(username, password))
      );

      if (!ok.isPresent()) return;
      if (ok.get()) break;

      Printer.out("{c,RED;Invalid username or password, please try again!}\n");
    }

    SingleMenu<String> menu = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "Select an action you want to perform: ",
      "Create/update/remove movie listing",
      "Create/update/remove showtime",
      "Configure system settings"
    );


    Component.loop(() -> {
      Console.clear();
      return menu;
    }, SingleMenu.map(
      () -> MovieManager.exec(db.getMovieDb()),
      () -> ShowTimeManager.exec(db.getShowTimeDb(), db.getCineplexes(), db.getMovieDb()),
      () -> new CreateEdit<>(new ConfigInput(db.getTicketPrice())).edit(i -> "system configuration")
    ));
  }
}
