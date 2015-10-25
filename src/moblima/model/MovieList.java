package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

public class MovieList implements Serializable {
    /**
     * Auto-incremented ID which will be assigned to the next movie
     */
    private AutoIncrement movieId = new AutoIncrement();

    /**
     * List of all movies
     */
    private List<Movie> movies = new ArrayList<>();
    private transient ShowTimeList showTimeList;

    public MovieList(ShowTimeList showTimeList) {
        this.showTimeList = showTimeList;
    }

    /**
     * Add a new movie to the list
     * @param title self-explanatory
     * @param types self-explanatory
     * @param genres self-explanatory
     * @param synopsis self-explanatory
     * @param director self-explanatory
     * @param casts self-explanatory
     * @param showingStatus self-explanatory
     * @return The newly added movie
     */
    public Movie add(String title, EnumSet<Movie.Type> types, String genres, String synopsis, String director,
                     List<String> casts, Movie.ShowingStatus showingStatus) {
        Movie newMovie = new Movie(movieId.getNext(), title, types, genres, synopsis, director, casts, showingStatus);
        movies.add(newMovie);
        return newMovie;
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
                .map(movie -> new Pair<>(movie, showTimeList.getBookingList().getForMovie(movie.getId(), showTimeList).count()))
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
        for (Integer showTimeId: showTimeList.getForMovie(id).map(ShowTime::getId).toArray(Integer[]::new)) {
            showTimeList.remove(showTimeId);
        }

        movies.remove(index);
    }

    public Stream<Movie> getAll() {
        return movies.stream();
    }
}
