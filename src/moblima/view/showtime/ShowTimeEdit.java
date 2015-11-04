package moblima.view.showtime;

import jni.Console;
import moblima.model.Cineplex;
import moblima.model.MovieDb;
import moblima.model.ShowTime;
import moblima.view.Component;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;

import java.util.List;
import java.util.function.Consumer;

public class ShowTimeEdit extends Component {

	public ShowTimeEdit(Component parent, ShowTime showTime, List<Cineplex> cineplexes, MovieDb movieDb) {
		super(parent);
		ShowTimeInput input = new ShowTimeInput(this, showTime, cineplexes, movieDb);

		attach(new SingleMenu(
			this,
			(choice, ignored) -> {
				Consumer<Runnable> fn = null;
				switch (choice) {
					case 0:
						fn = input::askMovie;
						break;
					case 1:
						fn = input::askCinema;
						break;
					case 2:
						fn = input::askStartTime;
						break;
					default:
						assert false;
				}
				fn.accept(() -> {
					System.out.println("Saved! Press any key to continue...");
					Console.getChar();
					renderFocus();
				});
			},
			MenuStr.Orientation.Vertical,
			String.format("Editing show time ID %d. Select an item: ", showTime.getId()),
			"Movie",
			"Cinema",
			"Start time"
		));
	}
}
