package moblima.view.moviegoer;

import moblima.model.Review;
import moblima.view.Printer;
import util.Strings;

/**
 * {@code ReviewPrinter} prints a {@link Review} to a formatting string which can then be printed by {@link Printer}.
 */
public class ReviewPrinter {
  private Review review;

  /**
   * Constructs the printer with a {@link Review} to print.
   *
   * @param review the review to print
   */
  public ReviewPrinter(Review review) {
    this.review = review;
  }

  /**
   * Prints the {@code review} to a string.
   *
   * @return the formatting string
   */
  @Override
  public String toString() {
    return String.format("%-30s (%d star%c {c,MAGENTA;%s})\n%s",
      review.getUsername(),
      review.getRating(),
      review.getRating() != 1 ? 's' : ' ',
      Strings.repeat('*', review.getRating()),
      review.getText()
    );
  }
}
