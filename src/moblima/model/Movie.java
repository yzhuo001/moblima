package moblima.model;

import util.Enums;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * {@code Movie} represents a movie in the database.
 */
public class Movie implements Serializable, Comparable<Movie> {
  int id;
  private String title;
  private Set<Genre> genres = EnumSet.noneOf(Genre.class);
  private Classification classification = Classification.GENERAL;
  private String synopsis;
  private String director;
  private int length;
  private String casts;
  private List<Review> reviews = new ArrayList<>();
  private ShowingStatus showingStatus = ShowingStatus.NOW_SHOWING;

  public Movie() {
  }

  public Movie(int id) {
    this.id = id;
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

  public Set<Genre> getGenres() {
    return genres;
  }

  public void setGenres(Set<Genre> genres) {
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

  public String getCasts() {
    return casts;
  }

  public String setCasts(String castsStr) {
    List<String> casts = Arrays.stream(castsStr.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    if (casts.size() < 2) {
      return "There should be at least 2 casts!";
    }
    this.casts = String.join(", ", casts);
    return null;
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

  public int getLength() {
    return length;
  }

  public String setLength(int length) {
    if (length <= 0) {
      return "Length should be positive";
    }
    this.length = length;
    return null;
  }

  public float getAverageRating() {
    if (reviews.size() == 0) {
      return Float.NaN;
    }

    return (float) reviews.stream().mapToInt(Review::getRating).sum() / reviews.size();
  }

  public void addReview(Review review) {
    reviews.add(review);
  }

  public Classification getClassification() {
    return classification;
  }

  public void setClassification(Classification classification) {
    this.classification = classification;
  }

  @Override
  public int compareTo(Movie o) {
    return Integer.compare(getId(), o.getId());
  }

  @Override
  public String toString() {
    return title;
  }

  public enum ShowingStatus {
    NOW_SHOWING,
    PREVIEW,
    COMING_SOON,
    END_OF_SHOWING;

    @Override
    public String toString() {
      return Enums.prettyPrint(super.toString());
    }
  }

  public enum Classification {
    GENERAL,
    PG,
    PG13,
    NC16,
    M18,
    R21;

    @Override
    public String toString() {
      switch (this) {
        case GENERAL:
          return "General";
        default:
          return super.toString();
      }
    }
  }

  /**
   * The genres for movies.
   */
  public enum Genre {
    ACTION,
    ADVENTURE,
    ANIMATION,
    DRAMA,
    MYSTERY,
    SCI_FI,
    COMEDY,
    HORROR,
    THRILLER,
    BLOCKBUSTER,
    THREE_DIMENSION;


    @Override
    public String toString() {
      switch (this) {
        case THREE_DIMENSION:
          return "3D";

        case SCI_FI:
          return "Sci-fi";

        default:
          return Enums.prettyPrint(super.toString());
      }
    }
  }
}
