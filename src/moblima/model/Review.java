package moblima.model;

import java.io.Serializable;

public class Review implements Serializable {
    private String username;
    private int rating;
    private String text;

    public Review(String username, int rating, String text) {
        this.username = username;
        this.rating = rating;
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public static int getMinRating() {
        return 1;
    }

    public static int getMaxRating() {
        return 5;
    }
}
