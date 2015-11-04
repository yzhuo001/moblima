package moblima.view;

import moblima.model.Database;
import moblima.model.Movie;
import moblima.view.menu.PagedMenu;
import moblima.view.menu.SingleMenu;
import moblima.view.movie.MovieMenu;

public class MovieGoerSection extends Component {
	private final Database db;

	public MovieGoerSection(Component parent, Database db) {
		super(parent);
		this.db = db;
		attach(new SingleMenu(
			this,
			this::onMainMenuAccept,
			"Select an action you want to perform:",
			"View list of movies",
			"View your booking history"
		));
	}

	private void onMainMenuAccept(int choice, Object __) {
		switch (choice) {
			case 0:
				new MovieMenu(
					this,
					movie -> new SingleMenu(
						this,
						(c, ___) -> onMovieMenuAccept(c, movie),
						String.format("You have selected %s. What do you want to do next?", movie),
						"Book a ticket",
						"Rate this movie",
						"See all reviews for this movie"
					).renderFocus(),
					db.getMovieDb().getAll()
				).renderFocus();

			case 1:
				break;
		}
	}

	private void onMovieMenuAccept(int choice, Movie movie) {
		switch (choice) {
			case 0:
				new SingleMenu(
					this,
					(i, o) -> {},
					String.format("Select a show time for %s: ", movie),
					db.getShowTimeDb().getAvailableForMovie(movie.getId())
				).renderFocus();
				break;

			case 1:
				new RateMovie(this, movie).renderFocus();
				break;

			case 2:
				System.out.printf("Reviews for %s\n", movie);
				new PagedMenu(
					this,
					(__, ___) -> {},
					5,
					movie.getReviews().stream().map(ReviewItem::new).toArray(ReviewItem[]::new)
				).renderFocus();
				break;
		}
	}

}
