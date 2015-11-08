package moblima.view;

import jni.Console;
import moblima.model.Database;
import moblima.view.admin.AdminSection;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;
import moblima.view.moviegoer.MovieGoerSection;

/**
 * {@code Home} screen displays the application name and allows the user to select which section to enter.
 */
public class Home {
  /**
   * Displays the screen.
   *
   * @param db the application database
   */
  public static void exec(Database db) {
    SingleMenu<String> menu = new SingleMenu<>(
      Menu.Orientation.Horizontal,
      "{m,40,4;Select a section: }",
      "Admin",
      "Movie Goer"
    );

    Component.loop(() -> {
      Console.clear();
      Printer.out("{m,30,0;{c,GREEN;" +
        "==========================================================\n" +
        "Group 6 - {c,RED;MO}vie {c,RED;B}ooking and {c,RED;L}isting {c,RED;M}anagement {c,RED;A}pplication\n" +
        "==========================================================\n}}");
      return menu;
    }, SingleMenu.map(
      () -> AdminSection.exec(db),
      () -> MovieGoerSection.exec(db)
    ));
  }
}
