package moblima.view.movie;

import moblima.model.Movie;
import moblima.model.MovieDb;
import moblima.util.Functions;
import moblima.view.Component;
import moblima.view.Util;

public class MovieNew extends Component {
	private final MovieDb movieDb;
	private final MovieInput input;

	public MovieNew(Component parent, MovieDb movieDb) {
		super(parent);
		this.movieDb = movieDb;
		input = new MovieInput(new Movie(), parent);
	}

	@Override
	public void onFocus() {
		Functions.chain(
			() -> {
				Movie movie = input.getMovie();
				movieDb.add(movie);
				Util.pause(String.format("%s has been added successfully", movie));
				cancel();
			},
			input::askTitle, input::askGenres, input::askClassification, input::askShowingStatus, input::askLength,
			input::askDirector, input::askCasts, input::askSynopsis
		).run();
	}
}