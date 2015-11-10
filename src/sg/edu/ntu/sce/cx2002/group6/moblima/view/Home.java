package sg.edu.ntu.sce.cx2002.group6.moblima.view;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.moviegoer.MovieGoerSection;
import sg.edu.ntu.sce.cx2002.group6.moblima.model.Database;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.admin.AdminSection;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.Menu;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.SingleMenu;

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
