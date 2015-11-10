package sg.edu.ntu.sce.cx2002.group6.moblima.view.admin;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.Util;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.Menu;
import sg.edu.ntu.sce.cx2002.group6.moblima.view.menu.SingleMenu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@code CreateEdit} utilizes an {@link Input} object to create or edit an entity.
 *
 * @param <T> the type of the entity object
 */
public class CreateEdit<T> {
  private final Input<T> input;
  private final List<Method> methods;
  private SingleMenu<String> editMenu;

  /**
   * Constructs a new {@code CreateEdit} on an entity.
   *
   * @param input the {@link Input} object for the entity
   */
  public CreateEdit(Input<T> input) {
    this.input = input;
    methods = Arrays.stream(input.getClass().getMethods())
      .filter(method -> method.getAnnotation(Input.Order.class) != null &&
        !Objects.equals(method.getName(), "get") &&
        Modifier.isPublic(method.getModifiers()) &&
        !Modifier.isStatic(method.getModifiers()))
      .sorted((m1, m2) ->
        Integer.compare(m1.getAnnotation(Input.Order.class).value(), m2.getAnnotation(Input.Order.class).value()))
      .collect(Collectors.toList());
  }

  private static String printableName(String methodName) {
    String name = methodName.chars().boxed()
      .map(c -> Character.isUpperCase(c) ? String.format(" %c", Character.toLowerCase(c)) : String.format("%c", c))
      .collect(Collectors.joining());
    return Character.toUpperCase(name.charAt(0)) + name.substring(1);
  }

  private void makeEditMenu() {
    if (editMenu != null) {
      return;
    }

    List<String> options = methods.stream()
      .map(Method::getName)
      .map(CreateEdit::printableName)
      .collect(Collectors.toList());

    editMenu = new SingleMenu<>(
      Menu.Orientation.Vertical,
      "",
      options
    );
  }

  private Optional<Object> invoke(int i) {
    try {
      return (Optional<Object>) methods.get(i).invoke(input);
    } catch (IllegalAccessException | InvocationTargetException e) {
      assert false;
      return null;
    }
  }

  /**
   * Displays a list of editable properties of the entity and calls the corresponding method in the
   * {@link Input} object to edit the property.
   *
   * @param name a function taking the entity object and returning a short representation of the object
   */
  public void edit(Function<T, String> name) {
    makeEditMenu();
    SingleMenu.loop(() -> {
      Console.clear();
      editMenu.setMessage(String.format("Editing %s. Select an item to edit: ", name.apply(input.get())));
      return editMenu;
    }, r -> invoke(r.first).ifPresent(i -> Util.pause("Saved")));
  }

  /**
   * Composes methods in the input object in a specified order to create a new entity object.
   *
   * @return an {@link Optional} object describing the new entity or an empty {@code Optional} if rejected
   */
  public Optional<T> create() {

    Optional<Object> chain = invoke(0);

    for (int i = 1; i < methods.size(); ++i) {
      final int finalI = i;
      chain = chain.flatMap(ig -> invoke(finalI));
    }

    return chain.map(ig -> input.get());
  }
}
