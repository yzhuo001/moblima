package moblima.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ShowTime implements Serializable, Comparable<ShowTime> {
    int id;
    int movieId;
    int cinemaId;
    LocalDateTime startTime;

    ShowTime(int id) {
        this.id = id;
    }

    ShowTime(int id, int movieId, int cinemaId, LocalDateTime startTime) {
        this.id = id;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.startTime = startTime;
    }

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

    @Override
    public int compareTo(ShowTime o) {
        return Integer.compare(getId(), o.getId());
    }
}
