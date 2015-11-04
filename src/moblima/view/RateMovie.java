package moblima.view;

import moblima.model.Movie;
import moblima.model.Review;
import moblima.util.Functions;
import moblima.view.menu.SingleMenu;

import java.util.stream.IntStream;

public class RateMovie extends Component {
	private final Movie movie;
	private final Review review = new Review();

	public RateMovie(Component parent, Movie movie) {
		super(parent);
		this.movie = movie;
	}

	private void askName(Runnable then) {
		new LineEdit(
			this,
			line -> { review.setUsername(line); then.run(); },
			"Enter your name: "
		).renderFocus();
	}

	private void askRating(Runnable then) {
		new SingleMenu(
			this,
			(i, star) -> { review.setRating((Integer) star); then.run(); },
			"How many stars would you give this movie? ",
			(Object[]) IntStream.range(Review.MIN_RATING, Review.MAX_RATING + 1).boxed().toArray(Integer[]::new)
		).renderFocus();
	}

	private void askReview(Runnable then) {
		new LineEdit(
			this,
			text -> { review.setText(text); then.run(); },
			"Enter a detailed review for this movie: "
		).renderFocus();
	}

	@Override
	public void onFocus() {
		Functions.chain(
			() -> {
				movie.addReview(review);
				cancel();
			},
			this::askName, this::askRating, this::askReview
		).run();
	}
}
