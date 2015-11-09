package moblima.model;

import util.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * {@code ShowTime} stores all information related to a show time.
 */
public class ShowTime implements Serializable, Comparable<ShowTime> {
  int id;
  int movie;
  LocalDateTime startTime;
  private Pair<Integer, Integer> cineplexCinemaPair = new Pair<>(0, 0);

  /**
   * Constructs a new show time associated with the given ID.
   *
   * @param id the id
   */
  ShowTime(int id) {
    this.id = id;
  }


  /**
   * Constructs an empty show time.
   */
  public ShowTime() {
  }

  /**
   * Gets the associated ID.
   *
   * @return the associated ID
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the associated movie.
   *
   * @return the the associated movie
   */
  public int getMovie() {
    return movie;
  }

  /**
   * Sets the associated movie.
   *
   * @param movie the associated movie
   */
  public void setMovie(int movie) {
    this.movie = movie;
  }

  /**
   * Gets the start time.
   *
   * @return the start time
   */
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * Sets the start time.
   *
   * @param startTime the start time
   */
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  /**
   * Gets the cinema object for this show time.
   *
   * @param cineplexes the list all cineplexes
   * @return the cinema object
   */
  public Cinema getCinema(List<Cineplex> cineplexes) {
    return cineplexes.get(cineplexCinemaPair.first).getCinemas().get(cineplexCinemaPair.second);
  }

  /**
   * Compares 2 show times by ID.
   */
  @Override
  public int compareTo(ShowTime o) {
    return Integer.compare(getId(), o.getId());
  }

  /**
   * Gets the (cineplex ID, cinema ID) pair for this show time.
   *
   * @return the (cineplex ID, cinema ID) pair
   */
  public Pair<Integer, Integer> getCineplexCinema() {
    return cineplexCinemaPair;
  }

  /**
   * Sets the cineplex and the cinema for this show time.
   *
   * @param cineplexCinemaPair the (cineplex ID, cinema ID) pair
   */
  public void setCineplexCinema(Pair<Integer, Integer> cineplexCinemaPair) {
    this.cineplexCinemaPair = cineplexCinemaPair;
  }
}
