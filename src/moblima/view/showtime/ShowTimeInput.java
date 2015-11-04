package moblima.view.showtime;

import moblima.model.*;
import moblima.view.Component;
import moblima.view.DateTimeEdit;
import moblima.view.menu.SingleMenu;

import java.util.List;

class ShowTimeInput {
	List<Cineplex> cineplexes;
	MovieDb movieDb;
	Component parent;
	ShowTime showTime;

	public ShowTimeInput(Component parent, ShowTime showTime, List<Cineplex> cineplexes, MovieDb movieDb) {
		this.showTime = showTime;
		this.cineplexes = cineplexes;
		this.movieDb = movieDb;
		this.parent = parent;
	}

	public ShowTime getShowTime() {
		return showTime;
	}

	public void askMovie(Runnable then) {
		new SingleMenu(
			parent,
			(ignored, movie) -> {
				showTime.setMovieId(((Movie) movie).getId());
				then.run();
			},
			"Select a movie: ",
			(Object[]) movieDb.getAll().toArray(Movie[]::new)
		).renderFocus();
	}

	private void askCineplex(Runnable then) {
		new SingleMenu(
			parent,
			(ignored, cineplex) -> {
				showTime.setCineplexId(((Cineplex) cineplex).getId());
				then.run();
			},
			"Select a cineplex: ",
			(Object[]) cineplexes.stream().toArray(Cineplex[]::new)
		).renderFocus();
	}

	public void askCinema(Runnable then) {
		askCineplex(() ->
				new SingleMenu(
					parent,
					(ignored, cinema) -> {
						showTime.setCinemaId(((Cinema) cinema).getId());
						then.run();
					},
					"Select a cinema: ",
					(Object[]) cineplexes.get(showTime.getCineplexId()).getCinemas().stream().toArray(Cinema[]::new)
				).renderFocus()
		);
	}

	public void askStartTime(Runnable then) {
		new DateTimeEdit(
			parent,
			dateTime -> {
				showTime.setStartTime(dateTime);
				then.run();
			},
			"Enter start time: "
		).renderFocus();
	}
}