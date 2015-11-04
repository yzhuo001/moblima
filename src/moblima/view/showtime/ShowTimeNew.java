package moblima.view.showtime;

import moblima.model.Cineplex;
import moblima.model.MovieDb;
import moblima.model.ShowTime;
import moblima.model.ShowTimeDb;
import moblima.util.Functions;
import moblima.view.Component;
import moblima.view.Util;

import java.util.List;

public class ShowTimeNew extends Component {
	private final ShowTimeDb showtimeDb;
	private final List<Cineplex> cineplexes;
	private final MovieDb movieDb;
	private final ShowTimeInput input;

	public ShowTimeNew(Component parent, ShowTimeDb showtimeDb, List<Cineplex> cineplexes, MovieDb movieDb) {
		super(parent);
		this.showtimeDb = showtimeDb;
		this.cineplexes = cineplexes;
		this.movieDb = movieDb;
		this.input = new ShowTimeInput(parent, new ShowTime(), cineplexes, movieDb);
	}

	@Override
	public void onFocus() {
		Functions.chain(() -> {
				String str = input.getShowTime().toString(cineplexes, movieDb);
				if (!showtimeDb.add(input.getShowTime(), movieDb)) {
					Util.pause(String.format("Clash deleted. The show time %s is not added", str));
				} else {
					Util.pause(String.format("Successfully added %s", str));
				}
				cancel();
			},
			input::askMovie,
			input::askCinema,
			input::askStartTime
		).run();
	}
}
