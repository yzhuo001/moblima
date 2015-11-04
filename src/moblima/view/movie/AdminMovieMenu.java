package moblima.view.movie;

import jni.Console;
import jni.Key;
import moblima.model.Movie;
import moblima.model.MovieDb;
import moblima.view.Component;
import moblima.view.Util;
import moblima.view.menu.SingleMenu;

public class AdminMovieMenu extends Component {
	private final MovieDb movieDb;
	private MovieMenu menu;

	public AdminMovieMenu(Component parent, MovieDb movieDb) {
		super(parent);
		this.movieDb = movieDb;
		updateMenu();
	}

	private void updateMenu() {
		menu = new MovieMenu(
			this,
			movie -> new MovieEdit(this, movie).renderFocus(),
			movieDb.getAll()
		);
		attach(menu);
	}

	@Override
	public boolean doOnKey(char key) {
		if (key != Key.DELETE) {
			return false;
		}

		Console.clear();
		Movie movie = menu.getActiveMovie();

		new SingleMenu(
			this,
			(choice, ignored) -> {
				if (choice == 1) {
					movieDb.remove(movie.getId());
					Util.pause(String.format("%s has been deleted", movie));
					updateMenu();
				}
				renderFocus();
			},
			String.format("Are you sure you want to delete %s?", movie),
			"No",
			"Yes"
		).renderFocus();
		return true;
	}
}
