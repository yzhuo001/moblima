package moblima.view.movie;

import moblima.model.Movie;
import moblima.view.Component;
import moblima.view.LineEdit;
import moblima.view.menu.MenuStr;
import moblima.view.menu.MultipleMenu;
import moblima.view.menu.SingleMenu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MovieInput {
	private Movie movie;
	private Component parent;

	public MovieInput(Movie movie, Component parent) {
		this.movie = movie;
		this.parent = parent;
	}

	public Movie getMovie() {
		return movie;
	}

	public void askTitle(Runnable then) {
		simpleAsk("Title", then);
	}

	public void askGenres(Runnable then) {
		MultipleMenu menu = new MultipleMenu(
			parent,
			choices -> {
				choices.stream().forEach(v -> movie.getGenres().add(((Movie.Genre) v.getSecond())));
				then.run();
			},
			MenuStr.Orientation.Horizontal,
			"Select one or more genres: ",
			(Object[]) Movie.Genre.values()
		);

		movie.getGenres().stream().forEach(genre -> menu.select(genre.ordinal()));
		menu.renderFocus();
	}

	public void askClassification(Runnable then) {
		SingleMenu menu = new SingleMenu(
			parent,
			(ignored, classification) -> {
				movie.setClassification((Movie.Classification) classification);
				then.run();
			},
			"Select movie classification: ",
			(Object[]) Movie.Classification.values()
		);
		menu.setActive(movie.getClassification().ordinal());
		menu.renderFocus();
	}

	public void askShowingStatus(Runnable then) {
		SingleMenu menu = new SingleMenu(
			parent,
			(ignored, status) -> {
				movie.setShowingStatus((Movie.ShowingStatus) status);
				then.run();
			},
			"Select showing status: ",
			(Object[]) Movie.ShowingStatus.values()
		);
		menu.setActive(movie.getShowingStatus().ordinal());
		menu.renderFocus();
	}

	public void askLength(Runnable then) {
		new LineEdit(
			parent,
			line -> {
				try {
					int length = Integer.parseInt(line);

					if (length > 0) {
						movie.setLength(length);
						then.run();
						return null;
					}
				} catch (Exception ignored) {
				}
				return "Movie length should be an integer greater than 0. Try again!";
			},
			"Length in minutes: ",
			String.valueOf(movie.getLength())
		).renderFocus();
	}


	public void askCasts(Runnable then) {
		new LineEdit(
			parent,
			line -> {
				String[] casts = line.split(",");
				if (casts.length < 2) {
					return "There be should at least 2 casts. Try again!";
				}

				movie.getCasts().addAll(Arrays.asList(casts).stream().map(String::trim).collect(Collectors.toList()));
				then.run();
				return null;
			},
			"Enter 2 or more casts, separated by ,: ",
			String.join(", ", movie.getCasts())
		).renderFocus();
	}

	public void askDirector(Runnable then) {
		simpleAsk("Director", then);
	}

	public void askSynopsis(Runnable then) {
		simpleAsk("Synopsis", then);
	}

	private void simpleAsk(String prop, Runnable then) {
		try {
			new LineEdit(
				parent,
				then,
				movie,
				prop,
				((String) movie.getClass().getMethod("get" + prop).invoke(movie))
			).renderFocus();
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			assert false;
		}
	}
}
