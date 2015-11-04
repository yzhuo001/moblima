package moblima.view.showtime;

import moblima.model.Cineplex;
import moblima.model.MovieDb;
import moblima.model.ShowTimeDb;
import moblima.view.Component;
import moblima.view.menu.SingleMenu;

import java.util.List;

public class ShowTimeManager extends Component {
	private final List<Cineplex> cineplexes;
	private final MovieDb movieDb;
	private ShowTimeDb showTimeDb;

	public ShowTimeManager(Component parent, ShowTimeDb showTimeDb, List<Cineplex> cineplexes, MovieDb movieDb) {
		super(parent);
		this.showTimeDb = showTimeDb;
		this.cineplexes = cineplexes;
		this.movieDb = movieDb;

		attach(new SingleMenu(
			this,
			(choice, ignored) -> {
				switch (choice) {
					case 0:
						addShowTime();
						break;

					case 1:
						new ShowTimeList(this, cineplexes, showTimeDb, movieDb).renderFocus();
						break;
				}
			},
			"Select an action you want to perform: ",
			"Create new show time",
			"View show time list"
		));
	}

	private void addShowTime() {
		new ShowTimeNew(this, showTimeDb, cineplexes, movieDb).renderFocus();
	}
}
