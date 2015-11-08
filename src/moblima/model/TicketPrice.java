package moblima.model;

import util.Enums;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TicketPrice implements Serializable {
  static private final int CHILDREN_END = 14;
  static private final int SENIOR_START = 40;
  private int basePrice;
  private HashMap<Multiplier, Float> multipliers = new HashMap<>();
  private HashSet<LocalDate> holidays = new HashSet<>();

  public TicketPrice(int basePrice) {
    this.basePrice = basePrice;
  }

  public int getBasePrice() {
    return basePrice;
  }

  public String setBasePrice(int basePrice) {
    if (basePrice <= 0) {
      return "Base price should be positive";
    }
    this.basePrice = basePrice;
    return null;
  }

  public float getMultiplier(Multiplier multiplier) {
    return multipliers.getOrDefault(multiplier, 1.f);
  }

  public String setMultiplier(Multiplier multiplier, float value) {
    if (value <= 0.f) {
      return "Multiplier should be positive";
    }

    multipliers.put(multiplier, value);
    return null;
  }

  public HashSet<LocalDate> getHolidays() {
    return holidays;
  }

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
