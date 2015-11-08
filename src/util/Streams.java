package util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utilities for streams.
 */
public class Streams {
  /**
   * Converts an iterator to a stream.
   *
   * @param <T>      the type of the iterator
   * @param iterator the iterator
   * @return the stream
   */
  public static <T> Stream<T> iteratorToStream(Iterator<T> iterator) {
    Iterable<T> iterable = () -> iterator;
    return StreamSupport.stream(iterable.spliterator(), false);
  }
}
