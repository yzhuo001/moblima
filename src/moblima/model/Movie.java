package moblima.model;

import moblima.util.Enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class Movie implements Serializable, Comparable<Movie> {
	int id;
	private String title;
	private EnumSet<Genre> genres = EnumSet.noneOf(Genre.class);
	private Classification classification = Classification.GENERAL;
	private String synopsis;
	private String director;
	private int length;
	private List<String> casts = new ArrayList<>();
	private List<Review> reviews = new ArrayList<>();
	private ShowingStatus showingStatus = ShowingStatus.NOW_SHOWING;

	public Movie() {
	}
	public Movie(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EnumSet<Genre> getGenres() {
		return genres;
	}

	public void setGenres(EnumSet<Genre> genres) {
		this.genres = genres;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public List<String> getCasts() {
		return casts;
	}

	public void setCasts(List<String> casts) {
		this.casts = casts;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public ShowingStatus getShowingStatus() {
		return showingStatus;
	}

	public void setShowingStatus(ShowingStatus showingStatus) {
		this.showingStatus = showingStatus;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public float getAverageRating() {
		if (reviews.size() == 0) {
			return Float.NaN;
		}

		return (float) reviews.stream().mapToInt(Review::getRating).sum() / reviews.size();
	}

	public void addReview(Review review) {
		reviews.add(review);
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	@Override
	public int compareTo(Movie o) {
		return Integer.compare(getId(), o.getId());
	}

	@Override
	public String toString() {
		return title;
	}

	public enum ShowingStatus {
		NOW_SHOWING,
		PREVIEW,
		COMING_SOON,
		END_OF_SHOWING;

		@Override
		public String toString() {
			return Enums.format(super.toString());
		}
	}

	public enum Classification {
		GENERAL,
		PG,
		PG13,
		NC16,
		M18,
		R21;

		@Override
		public String toString() {
			switch (this) {
				case GENERAL: return "General";
				default: return super.toString();
			}
		}
	}

	public enum Genre {
		ACTION,
		ADVENTURE,
		ANIMATION,
		DRAMA,
		MYSTERY,
		SCI_FI,
		COMEDY,
		HORROR,
		THRILLER,
		BLOCKBUSTER,
		THREE_DIMENSION;


		@Override
		public String toString() {
			switch (this) {
				case THREE_DIMENSION:
					return "3D";

				case SCI_FI:
					return "Sci-fi";

				default:
					return Enums.format(super.toString());
			}
		}
	}
}
