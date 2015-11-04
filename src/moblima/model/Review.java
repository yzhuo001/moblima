package moblima.model;

import java.io.Serializable;

public class Review implements Serializable {
	private String username;
	private int rating;
	private String text;

	public static final int MIN_RATING = 0;
	public static final int MAX_RATING = 5;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setText(String text) {
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
}
