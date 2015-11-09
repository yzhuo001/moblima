package moblima.model;

import util.Enums;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code TicketPrice} represents the price configuration for all cineplexes.
 */
public class TicketPrice implements Serializable {
  static private final int CHILDREN_END = 14;
  static private final int SENIOR_START = 40;
  private int basePrice;
  private HashMap<Multiplier, Float> multipliers = new HashMap<>();
  private HashSet<LocalDate> holidays = new HashSet<>();

  /**
   * Constructs a {@code TicketPrice} object with an initial base price.
   *
   * @param basePrice the base price
   */
  public TicketPrice(int basePrice) {
    this.basePrice = basePrice;
  }

  /**
   * Gets base price.
   *
   * @return the base price
   */
  public int getBasePrice() {
    return basePrice;
  }

  /**
   * Validates and sets base price.
   *
   * @param basePrice the base price
   * @return an error message if the given base price is invalid
   */
  public String setBasePrice(int basePrice) {
    if (basePrice <= 0) {
      return "Base price should be positive";
    }
    this.basePrice = basePrice;
    return null;
  }

  /**
   * Gets a multiplier value.
   *
   * @param multiplier the multiplier
   * @return the multiplier value
   */
  public float getMultiplier(Multiplier multiplier) {
    return multipliers.getOrDefault(multiplier, 1.f);
  }

  /**
   * Sets a multiplier value.
   *
   * @param multiplier the multiplier
   * @param value      the value
   * @return an error message if the given value is invalid.
   */
  public String setMultiplier(Multiplier multiplier, float value) {
    if (value <= 0.f) {
      return "Multiplier should be positive";
    }

    multipliers.put(multiplier, value);
    return null;
  }

  /**
   * Gets the set of all holidays.
   *
   * @return the set of holidays
   */
  public HashSet<LocalDate> getHolidays() {
    return holidays;
  }

  /**
   * Calculates discriminated price based on {@code basePrice} and {@code multipliers}.
   *
   * @param seat      the seat number
   * @param age       the customer age
   * @param rank      the cinema rank
   * @param genres    the movie genres
   * @param startTime the show time starting time
   * @return the calculated price
   */
  public float calculate(int seat, int age, Cinema.Rank rank, Set<Movie.Genre> genres, LocalDateTime startTime) {
    float price = basePrice;

    if (startTime.getHour() > 18) {
      price *= getMultiplier(Multiplier.AFTER_6);
    }

    if (startTime.getDayOfWeek().getValue() >= DayOfWeek.SATURDAY.getValue()) {
      price *= getMultiplier(Multiplier.WEEKEND);
    }

    if (holidays.contains(startTime.toLocalDate())) {
      price *= getMultiplier(Multiplier.PUBLIC_HOLIDAY);
    }

    if (genres.contains(Movie.Genre.THREE_DIMENSION)) {
      price *= getMultiplier(Multiplier.THREE_DIMENSION);
    }

    if (genres.contains(Movie.Genre.BLOCKBUSTER)) {
      price *= getMultiplier(Multiplier.BLOCKBUSTER);
    }

    if (rank == Cinema.Rank.PLATINUM) {
      price *= getMultiplier(Multiplier.PLATINUM_CINEMA);
    }

    if (age < CHILDREN_END) {
      price *= getMultiplier(Multiplier.CHILDREN);
    } else if (age >= SENIOR_START) {
      price *= getMultiplier(Multiplier.SENIOR_CITIZEN);
    }

    return price * Cinema.LAYOUT.at(seat);
  }


  /**
   * The multipliers used in price discrimination.
   */
  public enum Multiplier {
    CHILDREN,
    SENIOR_CITIZEN,
    WEEKEND,
    AFTER_6,
    PUBLIC_HOLIDAY,
    PLATINUM_CINEMA,
    BLOCKBUSTER,
    THREE_DIMENSION;

    @Override
    public String toString() {
      switch (this) {
        case AFTER_6:
          return "After 18:00";

        case THREE_DIMENSION:
          return "3D";

        default:
          return Enums.prettyPrint(super.toString());
      }
    }
  }
}
