package moblima.view.admin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@code Input} provides a set of view functions that allows the user to create/modify an entity object.
 *
 * @param <Entity> the type of the entity object
 */
public abstract class Input<Entity> {
  /**
   * This constant is to be used in the last {@link java.util.Optional#map(Function)} of Optional chaining to represent
   * a successful action.
   */
  protected static final Function<Object, Object> ok = r -> new Object();

  /**
   * This function is to be used in the last {@link java.util.Optional#map(Function)} of Optional chaining to represent
   * a successful action.
   */
  protected static <T> Function<T, Object> obj(Consumer<T> set) {
    return v -> {
      set.accept(v);
      return new Object();
    };
  }

  /**
   * Gets the associated entity.
   *
   * @return the entity
   */
  abstract public Entity get();

  /**
   * {@code Order} specifies order in which methods are called when an new entity is created.
   */
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Order {
    /**
     * Returns the order of the associated method
     *
     * @return an integer representing the order of the associated method.
     */
    int value();
  }
}
