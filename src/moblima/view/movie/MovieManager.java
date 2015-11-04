package moblima.view.movie;

import moblima.model.MovieDb;
import moblima.view.Component;
import moblima.view.Util;
import moblima.view.menu.SingleMenu;

public class MovieManager extends Component {
	private final MovieDb movieDb;

	public MovieManager(Component parent, MovieDb movieDb) {
		super(parent);
		attach(new SingleMenu(
			this,
			this::onAccept,
			"Select an action: ",
			"Add a new movie",
			"View and edit movie list"
		));
		this.movieDb = movieDb;
	}

	private void onAccept(int choice, Object ignored) {
		switch (choice) {
			case 0:
				new MovieNew(this, movieDb).renderFocus();
				break;

			case 1:
				new AdminMovieMenu(this, movieDb).renderFocus();
				break;

			default:
				assert false;
		}
	}
}
