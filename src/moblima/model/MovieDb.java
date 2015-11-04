package moblima.model;

import moblima.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MovieDb implements Serializable {
	private AutoIncrement movieId = new AutoIncrement();
	private List<Movie> movies = new ArrayList<>();
	transient ShowTimeDb showTimeDb;

	public MovieDb(ShowTimeDb showTimeDb) {
		this.showTimeDb = showTimeDb;
	}

	/**
	 * Adds a new movie to the list.
	 *
	 * @return ID of the newly added movie.
	 */
	public int add(Movie movie) {
		movie.id = movieId.getNext();
		movies.add(movie);
		return movie.id;
	}

	private int getIndex(int id) {
		return Collections.binarySearch(movies, new Movie(id));
	}

	public Stream<Movie> getTop5ByRating() {
		return movies.stream()
			.sorted((o1, o2) -> Float.compare(o1.getAverageRating(), o2.getAverageRating()))
			.limit(5);
	}

	public Stream<Pair<Movie, Long>> getTop5BySales() {
		return movies.stream()
			.map(movie -> new Pair<>(movie, showTimeDb.getBookingDb().getForMovie(movie.getId(), showTimeDb).count()))
			.sorted((o1, o2) -> Long.compare(o1.getSecond(), o2.getSecond()))
			.limit(5);
	}

	public void remove(int id) {
		int index = getIndex(id);
		if (index < 0) {
			return;
		}

		Movie movie = movies.get(index);

		//remove all show times for this movie
		showTimeDb.getForMovie(id).map(ShowTime::getId).forEach(showTimeDb::remove);

		movies.remove(index);
	}

	public Optional<Movie> get(int id) {
		int index = getIndex(id);
		return index < 0 ? Optional.<Movie>empty() : Optional.of(movies.get(index));
	}

	public Stream<Movie> getAll() {
		return movies.stream();
	}
}
