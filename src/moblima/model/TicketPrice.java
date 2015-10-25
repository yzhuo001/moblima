package moblima.model;

import java.io.Serializable;
import java.util.HashMap;

public class TicketPrice implements Serializable {
    private int basePrice;
    private float holidayMultiplier;
    private HashMap<Movie.Type, Float> movieTypeMultipliers;

    TicketPrice(int basePrice, float holidayMultiplier) {
        this.basePrice = basePrice;
        this.holidayMultiplier = holidayMultiplier;
        movieTypeMultipliers = new HashMap<>(Movie.Type.values().length);
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public float getHolidayMultiplier() {
        return holidayMultiplier;
    }

    public void setHolidayMultiplier(float holidayMultiplier) {
        this.holidayMultiplier = holidayMultiplier;
    }

    public float getMultiplierForMovieType(Movie.Type type) {
        return movieTypeMultipliers.getOrDefault(type, 1.f);
    }

    public void setMultiplierForMovieType(Movie.Type movieType, float multiplier) {
        movieTypeMultipliers.put(movieType, multiplier);
    }
}
