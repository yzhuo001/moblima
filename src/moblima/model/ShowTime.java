package moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ShowTime implements Serializable, Comparable<ShowTime> {
	int id;
	int movieId;
	int cineplexId;
	int cinemaId;
	LocalDateTime startTime;

	ShowTime(int id) {
		this.id = id;
	}


	public ShowTime() {}

	public int getId() {
		return id;
	}

	public int getMovieId() {
		return movieId;
	}

	public int getCinemaId() {
		return cinemaId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getCineplexId() {
		return cineplexId;
	}

	public void setCineplexId(int cineplexId) {
		this.cineplexId = cineplexId;
	}

	public Cinema getCinema(List<Cineplex> cineplexes) {
		return cineplexes.get(getCineplexId()).getCinemas().get(getCinemaId());
	}

	@Override
	public int compareTo(ShowTime o) {
		return Integer.compare(getId(), o.getId());
	}

	public String toString(List<Cineplex> cineplexes, MovieDb movieDb) {
		return String.format("%s %s at %s",
			movieDb.get(getMovieId()).get(),
			getStartTime(),
			getCinema(cineplexes)
		);
	}
}
