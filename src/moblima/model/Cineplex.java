package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cineplex.
 */
public class Cineplex implements Serializable {
  private int id;
  private List<Cinema> cinemas = new ArrayList<>();
  private String name;

  /**
   * Gets the cineplex id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the cinemas under this cineplex.
   *
   * @return the list of cinemas
   */
  public List<Cinema> getCinemas() {
    return cinemas;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Gets the cineplex name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }
}
