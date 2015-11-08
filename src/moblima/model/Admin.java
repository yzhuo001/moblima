package moblima.model;


import java.io.Serializable;

/**
 * The Admin entity object.
 */
public class Admin implements Serializable {
  private String username;
  private String password;

  /**
   * Constructs a new admin with username and password.
   *
   * @param username the username
   * @param password the password
   */
  public Admin(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Verify the username and password.
   *
   * @param username the username
   * @param password the password
   * @return {@code true} if {@code username} and {@code password} match the {@code username} and {@code password} stored
   * in this object, {@code false} otherwise
   */
  public boolean verify(String username, String password) {
    return this.username.equals(username) && this.password.equals(password);
  }
}
