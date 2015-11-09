package moblima.model;

import java.io.Serializable;

/**
 * {@code Review} represents a review made by a movie-goer.
 */
public class Review implements Serializable {
  /**
   * The minimum rating possible.
   */
  public static final int MIN_RATING = 0;
  /**
   * The maximum rating possible..
   */
  public static final int MAX_RATING = 5;
  private String username;
  private int rating;
  private String text;

  /**
   * Gets the rating.
   *
   * @return the rating
   */
  public int getRating() {
    return rating;
  }

  /**
   * Sets the rating.
   *
   * @param rating the rating
   */
  public void setRating(int rating) {
    this.rating = rating;
  }

  /**
   * Gets the detailed review.
   *
   * @return the detailed review
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the detailed review.
   *
   * @param text the detailed review
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets the user name.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user name.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
