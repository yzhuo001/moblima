package moblima.model;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * The Customer entity object.
 */
public class Customer implements Serializable {
  private static final Pattern EMAIL = Pattern.compile("\\b[a-z0-9._%+-]+@[a-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
  private String name;
  private String phone;
  private String email;
  private int age;

  /**
   * Gets the customer name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the customer name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the customer phone.
   *
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Validates and sets the customer phone.
   *
   * @param phone the phone
   * @return an error message explaining why the phone is invalid, or {@code null} if the phone is valid.
   */
  public String setPhone(String phone) {
    if (!phone.chars().allMatch(c -> Character.isSpaceChar(c) || Character.isDigit(c))) {
      return "Phone number can consist only of numbers and spaces.";
    }

    this.phone = phone;
    return null;
  }

  /**
   * Gets the customer email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Validates and sets the customer email.
   *
   * @param email the email
   * @return an error message if the email is invalid, or {@code null} if the email is valid.
   */
  public String setEmail(String email) {
    if (!EMAIL.matcher(email).matches()) {
      return "Invalid email.";
    }
    this.email = email;
    return null;
  }

  /**
   * Gets the customer age.
   *
   * @return the age
   */
  public int getAge() {
    return age;
  }

  /**
   * Sets the customer age.
   *
   * @param age            the age
   * @param classification the classification of the movie the customer is booking
   * @return an error message if the customer is underage, or {@code null} if the customer can see the movie.
   */
  public String setAge(int age, Movie.Classification classification) {
    int minAge = 0;

    switch (classification) {
      case GENERAL:
      case PG:
        minAge = 0;
        break;

      case PG13:
        minAge = 13;
        break;

      case NC16:
        minAge = 16;
        break;

      case M18:
        minAge = 18;
        break;

      case R21:
        minAge = 21;
        break;
    }

    if (age < minAge) {
      return String.format("You should be at least %d to see this movie.", minAge);
    }

    if (age > 116) {
      return "Aren't you a little too old to go to cinemas?";
    }

    this.age = age;
    return null;
  }
}
