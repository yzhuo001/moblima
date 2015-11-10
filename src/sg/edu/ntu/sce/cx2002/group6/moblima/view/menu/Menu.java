package sg.edu.ntu.sce.cx2002.group6.moblima.view.menu;

import sg.edu.ntu.sce.cx2002.group6.console.Key;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Component;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Printer;

import java.util.Arrays;
import java.util.List;

/**
 * {@code Menu} is the base {@code Component} for all kinds of menu. A menu presents a horizontal or vertical list of options.
 * The user selects an option by pressing enter.
 *
 * @param <OptionType> the type of all options
 * @param <ResultType> the result type returning by this {@code Menu}
 */
abstract public class Menu<OptionType, ResultType> extends Component<ResultType> {
  /**
   * The formatting string for active option.
   */
  protected final String ACTIVE_FMT = "{c,WHITE,DARKBLUE;%s}";
  /**
   * The menu orientation.
   */
  protected final Orientation orientation;
  /**
   * The index of the active option.
   */
  protected int active;
  /**
   * The list of options.
   */
  protected List<OptionType> options;
  private String optionFormat;
  private String message;
  private int prevKey;
  private int nextKey;

  /**
   * Constructs a new {@code Menu} with no parent.
   *
   * @param orientation the menu orientation
   * @param message     the message displayed before the list of options
   * @param options     the options
   */
  @SafeVarargs
  public Menu(Orientation orientation, String message, OptionType... options) {
    this(null, orientation, message, options);
  }

  /**
   * Constructs a new {@code Menu} with a parent.
   *
   * @param parent      the parent {@code Component}
   * @param orientation the message displayed before the list of options
   * @param message     the message
   * @param options     the options
   */
  @SafeVarargs
  public Menu(Component parent, Orientation orientation, String message, OptionType... options) {
    this(parent, orientation, message, Arrays.asList(options));
  }

  /**
   * Constructs a new {@code Menu} with no parent.
   *
   * @param orientation the menu orientation
   * @param message     the message displayed before the list of options
   * @param options     the options
   */
  public Menu(Orientation orientation, String message, List<OptionType> options) {
    this(null, orientation, message, options);
  }

  /**
   * Constructs a new {@code Menu} with a parent.
   *
   * @param parent      the parent {@code Component}
   * @param orientation the message displayed before the list of options
   * @param message     the message
   * @param options     the options
   */
  public Menu(Component parent, Orientation orientation, String message, List<OptionType> options) {
    super(parent);
    this.orientation = orientation;
    this.message = message;
    this.options = options;

    switch (orientation) {
      case Horizontal:
        prevKey = Key.LEFT;
        nextKey = Key.RIGHT;
        optionFormat = "%s    ";
        break;
      case Vertical:
        prevKey = Key.UP;
        nextKey = Key.DOWN;
        optionFormat = options.stream().anyMatch(op -> op.toString().contains("\n")) ? "%s"
          : "%-" +
          options.stream().map(Object::toString).map(String::length).max(Integer::compare).get() +
          "s";
        break;
    }
    active = 0;
  }

  @Override
  public boolean handleKey(char c) {
    if (c == prevKey || c == nextKey) {
      active = Math.floorMod(active + (c == prevKey ? -1 : 1), options.size());
      render();
    }

    return false;
  }

  /**
   * Subclasses re-implement this method to return a formatting string for an option.
   *
   * @param index the index of the option
   * @return the formatting string for {@code option}
   */
  abstract protected String getOption(int index);

  public void doRender() {
    Printer.out(message);
    if (orientation == Orientation.Vertical) {
      System.out.println();
    }

    for (int i = 0; i < options.size(); ++i) {
      Printer.outf(i == active ? ACTIVE_FMT : "%s", String.format(optionFormat, getOption(i)));

      if (orientation == Orientation.Vertical) {
        System.out.println();
      } else {
        System.out.print(" ");
      }
    }

    if (orientation == Orientation.Horizontal) {
      System.out.println();
    }
  }

  /**
   * Gets the index of the active option.
   *
   * @return the index of the active option
   */
  public int getActive() {
    return active;
  }

  /**
   * Sets active option.
   *
   * @param index index of the index option
   */
  public void setActive(int index) {
    this.active = index;
  }

  /**
   * Sets the message displayed before the list of options.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * The orientations for menus.
   */
  public enum Orientation {
    /**
     * Options will be represented as a horizontal list.
     */
    Horizontal,
    /**
     * Options will be represented as a vertical list.
     */
    Vertical
  }
}
