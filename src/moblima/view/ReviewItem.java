package moblima.view;

import jni.Color;
import jni.TextColor;
import moblima.model.Review;

import java.util.stream.IntStream;

public class ReviewItem implements SimpleComponent {
	private Review review;

	public ReviewItem(Review review) {
		this.review = review;
	}

	@Override
	public void render() {
		System.out.printf("%-50s", review.getUsername());
		try (TextColor ignored = new TextColor(Color.MAGENTA)) {
			IntStream.range(1, review.getRating() + 1).forEach(i -> System.out.print('♥'));
		}
		System.out.println(review.getText());
	}
}
