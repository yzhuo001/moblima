package moblima.view.showtime;

import jni.Console;
import moblima.model.Cineplex;
import moblima.model.MovieDb;
import moblima.model.ShowTime;
import moblima.model.ShowTimeDb;
import moblima.view.Component;
import moblima.view.Util;
import moblima.view.menu.DeletableMenu;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;

import java.util.List;

public class ShowTimeList extends Component {
	private final List<Cineplex> cineplexes;
	private final ShowTimeDb showTimeDb;
	private final MovieDb movieDb;
	SingleMenu menu;

	public ShowTimeList(Component parent, List<Cineplex> cineplexes, ShowTimeDb showTimeDb, MovieDb movieDb) {
		super(parent);
		this.cineplexes = cineplexes;
		this.showTimeDb = showTimeDb;
		this.movieDb = movieDb;
		updateMenu();
	}

	private String[] showTimeStrings() {
		return showTimeDb.getAll()
			.map(showTime -> showTime.toString(cineplexes, movieDb))
			.toArray(String[]::new);
	}

	private void updateMenu() {
		menu = showTimeDb.getAll().count() != 0 ? new DeletableMenu(
			this,
			(choice, ignored) -> new ShowTimeEdit(this, showTimeDb.getAt(choice), cineplexes, movieDb).renderFocus(),
			(choice, ignored) -> {
				showTimeDb.remove(showTimeDb.getAt(choice).getId());
				updateMenu();
				renderFocus();
			},
			MenuStr.Orientation.Vertical,
			"Select a showtime to edit/delete",
			(Object[]) showTimeStrings()
		) : null;
		attach(menu);
	}

	@Override
	public void render() {
		Console.clear();
		if (menu == null) {
			Util.pause("No show times have been added");
			cancel();
		} else {
			menu.render();
		}
	}
}
