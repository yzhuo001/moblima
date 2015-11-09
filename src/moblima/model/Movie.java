package moblima.model;

import util.Enums;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * {@code Movie} represents a movie in the {@link MovieDb}.
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

  /**
   * Constructs an empty movie.
   */
  public Movie() {
  }

  /**
   * Constructs a movie associated with an ID.
   *
   * @param id the ID
   */
  public Movie(int id) {
    this.id = id;
  }

  /**
   * Gets the movie id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the movie title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the movie title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the movie genres.
   *
   * @return the genres
   */
  public Set<Genre> getGenres() {
    return genres;
  }

  /**
   * Sets the movie genres.
   *
   * @param genres the genres
   */
  public void setGenres(Set<Genre> genres) {
    this.genres = genres;
  }

  /**
   * Gets the movie synopsis.
   *
   * @return the synopsis
   */
  public String getSynopsis() {
    return synopsis;
  }

  /**
   * Sets the movie synopsis.
   *
   * @param synopsis the synopsis
   */
  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  /**
   * Gets the movie director.
   *
   * @return the director
   */
  public String getDirector() {
    return director;
  }

  /**
   * Sets the movie director.
   *
   * @param director the director
   */
  public void setDirector(String director) {
    this.director = director;
  }

  /**
   * Gets the movie casts.
   *
   * @return the casts
   */
  public String getCasts() {
    return casts;
  }

  /**
   * Validates and sets the movie casts.
   *
   * @param castsStr the comma-separated string of casts
   * @return an error message if there are less than 2 casts
   */
  public String setCasts(String castsStr) {
    List<String> casts = Arrays.stream(castsStr.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    if (casts.size() < 2) {
      return "There should be at least 2 casts!";
    }
    this.casts = String.join(", ", casts);
    return null;
  }

  /**
   * Gets the list of reviews for this movie.
   *
   * @return the list of reviews
   */
  public List<Review> getReviews() {
    return reviews;
  }

  /**
   * Gets the movie showing status.
   *
   * @return the showing status
   */
  public ShowingStatus getShowingStatus() {
    return showingStatus;
  }

  /**
   * Sets the movie showing status.
   *
   * @param showingStatus the showing status
   */
  public void setShowingStatus(ShowingStatus showingStatus) {
    this.showingStatus = showingStatus;
  }

  /**
   * Gets the movie length.
   *
   * @return the length
   */
  public int getLength() {
    return length;
  }

  /**
   * Validates and sets the movie length.
   *
   * @param length the length
   * @return an error message if the length is invalid
   */
  public String setLength(int length) {
    if (length <= 0) {
      return "Length should be positive";
    }
    this.length = length;
    return null;
  }

  /**
   * Gets the movie average rating.
   *
   * @return an {@link Optional} object describing the average rating, or an empty {@code Optional} if there is less than
   * two review for this movie
   */
  public Optional<Float> getAverageRating() {
    if (reviews.size() < 2) {
      return Optional.empty();
    }

    return Optional.of((float) reviews.stream().mapToInt(Review::getRating).sum() / reviews.size());
  }

  /**
   * Adds a review.
   *
   * @param review the review
   */
  public void addReview(Review review) {
    reviews.add(review);
  }

  /**
   * Gets the movie classification.
   *
   * @return the classification
   */
  public Classification getClassification() {
    return classification;
  }

  /**
   * Sets the movie classification.
   *
   * @param classification the classification
   */
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

  /**
   * The showing statuses for movies.
   */
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

  /**
   * The classifications for movies.
   */
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
