package moblima.view;

import jni.Key;
import moblima.view.menu.Menu;
import moblima.view.menu.SingleMenu;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * The {@code DateTimeEdit} class provides a {@link Component} for editing dates and times.
 * {@code DateTimeEdit} allows the user to edit dates and times by the arrow keys to increase and decrease date and
 * time values.
 * {@code DateTimeEdit} always returns a instance of {@link LocalDateTime} when accepted regardless of its {@link Type}.
 * The caller can call {@link LocalDateTime#toLocalDate()} on the result to get the date-only representation.
 */
public class DateTimeEdit extends Component<LocalDateTime> {
  private final String label;
  private final Type type;
  private LocalDateTime dateTime;
  private SingleMenu menu;

  /**
   * Constructs a date editor with no parent. The value is set to current date time.
   * @param type     see {@link Type}
   * @param label  the label displayed before the editor
   */
  public DateTimeEdit(Type type, String label) {
    this(type, label, null);
  }

  /**
   * Constructs a date editor with no parent. The value is set to {@code dateTime}.
   *
   * @param type     see {@link Type}
   * @param label  the label displayed before the editor
   * @param dateTime the starting date time
   */
  public DateTimeEdit(Type type, String label, LocalDateTime dateTime) {
    this(null, type, label, dateTime);
  }

  /**
   * Constructs a date editor with a parent. The value is set current date time.
   * @param parent   the parent {@code Component}
   * @param type     see {@link Type}
   * @param label  the label displayed before the editor
   */
  public DateTimeEdit(Component parent, Type type, String label) {
    this(parent, type, label, null);
  }

  /**
   * Constructs a date editor with a parent. The value is set to {@code dateTime}.
   *
   * @param parent   the parent {@code Component}
   * @param type     see {@link Type}
   * @param label  the label displayed before the editor
   * @param dateTime the starting date time
   */
  public DateTimeEdit(Component parent, Type type, String label, LocalDateTime dateTime) {
    super(parent);
    this.dateTime = dateTime == null ? LocalDateTime.now() : dateTime;
    this.label = label;
    this.type = type;
    updateMenu();
  }

  private void updateMenu() {
    int active = 0;

    if (menu != null) {
      active = menu.getActive();
      menu.close();
    }

    String dow = String.format("%s%-10s", label, dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    String dom = String.format("%d%s", dateTime.getDayOfMonth(), Util.getDaySuffix(dateTime.getDayOfMonth()));
    String month = dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    String year = String.valueOf(dateTime.getYear());

    if (type == Type.DATE_ONLY) {
      menu = new SingleMenu<>(
        this,
        Menu.Orientation.Horizontal,
        dow,
        dom, month, year
      );
    } else {
      menu = new SingleMenu<>(
        this,
        Menu.Orientation.Horizontal,
        dow,
        dom, month, year,
        String.format("%d:", dateTime.getHour()),
        String.valueOf(dateTime.getMinute())
      );
    }

    menu.setActive(active);
    menu.onFinished(r -> setResult(dateTime));
  }

  @Override
  public boolean handleKey(char key) {
    switch (key) {
      case Key.UP:
        switch (menu.getActive()) {
          case 0: //day of month
            dateTime = dateTime.minusDays(1);
            break;

          case 1: //month
            dateTime = dateTime.minusMonths(1);
            break;

          case 2: //year
            dateTime = dateTime.minusYears(1);
            break;

          case 3: //hour
            dateTime = dateTime.minusHours(1);
            break;

          case 4: //minute
            dateTime = dateTime.minusMinutes(1);
            break;

          default:
            assert false;
        }
        updateMenu();
        render();
        return true;

      case Key.DOWN:
        switch (menu.getActive()) {
          case 0: //day of month
            dateTime = dateTime.plusDays(1);
            break;

          case 1: //month
            dateTime = dateTime.plusMonths(1);
            break;

          case 2: //year
            dateTime = dateTime.plusYears(1);
            break;

          case 3: //hour
            dateTime = dateTime.plusHours(1);
            break;

          case 4: //minute
            dateTime = dateTime.plusMinutes(1);
            break;

          default:
            assert false;
        }
        updateMenu();
        render();
        return true;

      default:
        return false;
    }
  }

  /**
   * This enum specifies which part of date time is editable.
   */
  public enum Type {
    /**
     * Only date is displayed and editable.
     */
    DATE_ONLY,
    /**
     * Both date and time are displayed and editable.
     */
    DATE_TIME
  }
}
