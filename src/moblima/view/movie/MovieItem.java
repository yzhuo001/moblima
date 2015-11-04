package moblima.view.movie;

import jni.Color;
import jni.TextColor;
import moblima.model.Movie;
import moblima.view.SimpleComponent;

import java.util.stream.Collectors;

public class MovieItem implements SimpleComponent {
	private Movie movie;

	public MovieItem(Movie movie) {
		this.movie = movie;
	}

	private static void printEntry(String label, String content) {
		try (TextColor ignored = new TextColor(Color.CYAN)) {
			System.out.print(label);
		}
		System.out.println(content);
	}

	public Movie getMovie() {
		return movie;
	}

	public void render() {
		//first line: title | showing status | rating
		try (TextColor ignored = movie.getShowingStatus() == Movie.ShowingStatus.NOW_SHOWING
			? new TextColor(Color.YELLOW, Color.DARKGREEN)
			: new TextColor(Color.BLACK, Color.DARKGRAY)) {
			System.out.print(movie.getShowingStatus());
		}
		System.out.print("     ");

		System.out.print(movie.getTitle().toUpperCase());
		System.out.print("     ");
		try (TextColor color = new TextColor(Color.MAGENTA)) {
			float rating = movie.getAverageRating();
			if (Float.isNaN(rating)) {
				System.out.println(" No ratings yet!");
			} else {
				System.out.printf(" Avarage rating: %.1f\n", rating);
			}
		}

		//second line: classification | length | genres
		try (TextColor color = new TextColor(Color.RED)) {
			System.out.print(movie.getClassification());
		}
		System.out.printf(" | %d min | ", movie.getLength());
		System.out.print(String.join(", ", movie.getGenres().stream().map(Object::toString).collect(Collectors.toList())));
		System.out.print("\n\n");

		System.out.println(movie.getSynopsis());

		System.out.print("\n");

		printEntry("Director: ", movie.getDirector());
		printEntry("Stars: ", String.join(", ", movie.getCasts()));
	}
}
