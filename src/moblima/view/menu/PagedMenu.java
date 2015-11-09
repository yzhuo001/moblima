package moblima.view.menu;

import jni.Key;
import moblima.view.Component;
import util.Pair;

import java.util.List;

/**
 * {@code PagedMenu} divides the list of options into pages. It returns returns a pair
 * {@code (selected option index, selected option)} when accepted.
 *
 * @param <OptionType> the of all options
 */
public class PagedMenu<OptionType> extends Component<Pair<Integer, OptionType>> {
  private final int optionsPerPage;
  private final String message;
  private final List<OptionType> options;
  private SingleMenu<OptionType> menu;
  private int page;

  /**
   * Constructs a new paged menu with no parent.
   *
   * @param optionsPerPage the number of options to display per page
   * @param message        the message to display before the list of options
   * @param options        the options
   */
  public PagedMenu(int optionsPerPage, String message, List<OptionType> options) {
    this(null, optionsPerPage, message, options);
  }

  /**
   * Constructs a new paged menu with a parent.
   *
   * @param parent         the parent {@link Component}
   * @param optionsPerPage the number of options to display per page
   * @param message        the message to display before the list of options
   * @param options        the options
   */
  public PagedMenu(Component parent, int optionsPerPage, String message, List<OptionType> options) {
    super(parent);
    this.optionsPerPage = optionsPerPage;
    this.message = message;
    this.options = options;
    setPage(0);
  }

  /**
   * Creates a new {@code SingleMenu} representing the given page.
   */
  private void setPage(int nextPage) {
    int totalPage = options.size() / optionsPerPage + 1;
    page = Math.floorMod(nextPage, totalPage);
    int start = page * optionsPerPage;
    int end = Math.min(start + optionsPerPage, options.size());
    if (menu != null) {
      menu.close();
    }
    menu = new SingleMenu<>(
      this,
      Menu.Orientation.Vertical,
      String.format("%s. Page %d/%d (entry %d/%d)", message, page + 1, totalPage, end, options.size()),
      options.subList(start, end)
    );

    menu.onFinished(this::setResult);
  }

  @Override
  public boolean handleKey(char c) {
    switch (c) {
      case Key.PAGE_DOWN:
        setPage(page + 1);
        render();
        return true;

      case Key.PAGE_UP:
        setPage(page - 1);
        render();
        return true;
    }

    return false;
  }
}
