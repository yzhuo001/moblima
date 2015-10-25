package moblima.model;

import java.awt.print.Book;
import java.io.Serializable;
import java.util.*;

public class Movie implements Serializable, Comparable<Movie> {

    public enum ShowingStatus {
        COMING_SOON,
        PREVIEW,
        NOW_SHOWING,
        END_OF_SHOWING
    }

    public enum Type {
        ThreeDimension,
        BlockBuster
    }

    int id;
    String title;
    EnumSet<Type> types;
    String genres;
    String synopsis;
    String director;
    List<String> casts = new ArrayList<>();
    List<Review> reviews = new ArrayList<>();
    ShowingStatus showingStatus;

    Movie(int id) {
        this.id = id;
    }

    Movie(int id, String title, EnumSet<Type> types, String genres, String synopsis, String director, List<String> casts, ShowingStatus showingStatus) {
        this.id = id;
        this.title = title;
        this.types = types;
        this.genres = genres;
        this.synopsis = synopsis;
        this.director = director;
        this.casts = casts;
        this.showingStatus = showingStatus;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getCasts() {
        return casts;
    }

    public void setCasts(List<String> casts) {
        this.casts = casts;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public ShowingStatus getShowingStatus() {
        return showingStatus;
    }

    public void setShowingStatus(ShowingStatus showingStatus) {
        this.showingStatus = showingStatus;
    }

    public float getAverageRating() {
        if (reviews.size() == 0) {
            return 0;
        }

        return (float)reviews.stream().mapToInt(Review::getRating).sum() / reviews.size();
    }

    @Override
    public int compareTo(Movie o) {
        return Integer.compare(getId(), o.getId());
    }
}
