package moblima.view.menu;

import jni.Key;
import moblima.view.Component;
import util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A {@code SingleMenu} is a {@link Menu} which allows the user to select only one option. {@code SingleMenu} returns a pair
 * {@code (selected option index, selected option)} when accepted.
 *
 * @param <OptionType> the type of all options
 */
public class SingleMenu<OptionType> extends Menu<OptionType, Pair<Integer, OptionType>> {

  /**
   * @see Menu#Menu(Orientation, String, Object[])
   */
  @SafeVarargs
  public SingleMenu(Orientation orientation, String message, OptionType... options) {
    super(orientation, message, options);
  }

  /**
   * @see Menu#Menu(Component, Orientation, String, Object[])
   */
  @SafeVarargs
  public SingleMenu(Component parent, Orientation orientation, String message, OptionType... options) {
    super(parent, orientation, message, options);
  }

  /**
   * @see Menu#Menu(Orientation, String, List)
   */
  public SingleMenu(Orientation orientation, String message, List<OptionType> options) {
    super(orientation, message, options);
  }

  /**
   * @see Menu#Menu(Component, Orientation, String, List)
   */
  public SingleMenu(Component parent, Orientation orientation, String message, List<OptionType> options) {
    super(parent, orientation, message, options);
  }

  /**
   * Returns a function which runs the nth function in the given list of functions when the nth option is selected.
   * This method is to be used with {@link Optional#ifPresent(Consumer)} on the {@code Optional} object returned
   * as a result of executing this component.
   *
   * @param fn the list of functions
   * @return the parameter that can be passed to {@link Optional#ifPresent(Consumer)}
   */
  public static Consumer<Pair<Integer, String>> map(Runnable... fn) {
    return r -> fn[r.first].run();
  }

  @Override
  public boolean handleKey(char c) {
    if (super.handleKey(c)) {
      return true;
    }

    if (c == Key.ENTER) {
      this.setResult(new Pair<>(active, options.get(active)));
      return true;
    }

    return false;
  }

  @Override
  protected String getOption(int index) {
    return options.get(index).toString();
  }
}