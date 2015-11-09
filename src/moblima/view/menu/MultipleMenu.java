package moblima.view.menu;

import jni.Key;
import moblima.view.Component;
import util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@code MultipleMenu} is a {@link Menu} which allows the user to select more than one option. The user selects an
 * option by pressing SPACE and accepts the selected options by pressing ENTER.
 * <p>
 * {@code MultipleMenu} returns a list of pairs (selected option index, selected option)
 *
 * @param <OptionType> the type of all options
 */
public class MultipleMenu<OptionType> extends Menu<OptionType, List<Pair<Integer, OptionType>>> {
  private final Set<Integer> selected = new HashSet<>();

  /**
   * @see Menu#Menu(Orientation, String, Object[])
   */
  @SafeVarargs
  public MultipleMenu(Orientation orientation, String message, OptionType... options) {
    super(orientation, message, options);
  }

  /**
   * @see Menu#Menu(Component, Orientation, String, Object[])
   */
  @SafeVarargs
  public MultipleMenu(Component parent, Orientation orientation, String message, OptionType... options) {
    super(parent, orientation, message, options);
  }

  /**
   * @see Menu#Menu(Orientation, String, List)
   */
  public MultipleMenu(Orientation orientation, String message, List<OptionType> options) {
    super(orientation, message, options);
  }

  /**
   * @see Menu#Menu(Component, Orientation, String, List)
   */
  public MultipleMenu(Component parent, Orientation orientation, String message, List<OptionType> options) {
    super(parent, orientation, message, options);
  }


  /**
   * Selects the nth option.
   *
   * @param index the index of the option to select
   */
  public void select(int index) {
    selected.add(index);
  }

  @Override
  public boolean handleKey(char c) {
    if (super.handleKey(c)) {
      return true;
    }

    switch (c) {
      case ' ':
        if (selected.contains(active)) {
          selected.remove(active);
        } else {
          selected.add(active);
        }
        render();
        return true;

      case Key.ENTER:
        if (selected.size() > 0) {
          this.setResult(
            selected.stream()
              .map(sel -> new Pair<>(sel, options.get(sel)))
              .collect(Collectors.toList())
          );
        }
        return true;
    }

    return false;
  }

  /**
   * Adds a green tick mark to the formatting string of the option if it is selected.
   */
  @Override
  protected String getOption(int index) {
    String str = options.get(index).toString();
    if (selected.contains(index)) {
      str = "{c,GREEN;√} " + str;
    }

    return str;
  }
}
