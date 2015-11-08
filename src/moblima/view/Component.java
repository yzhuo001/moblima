package moblima.view;

import jni.Console;
import jni.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * The {@code Component} class is the base class of all view objects. The {@code Component} can receive keyboard event
 * from the {@code Console} as well as render itself on the {@code Console} screen.
 *
 * @param <ResultType> the result type returned by this {@code Component}
 */
public abstract class Component<ResultType> implements Renderable, AutoCloseable {
  public static final char REJECTING_KEY = Key.ESC;
  private Component parent;
  private List<Component> children = new ArrayList<>();
  protected final Renderer renderer = new Renderer(this::doRender);
  private Consumer<ResultType> finishedListener = r -> {
  };
  private Optional<ResultType> result;

  /**
   * Construct a {@code Component}, with no parent.
   */
  public Component() {
    this(null);
  }

  /**
   * Construct a component which is a child of {@code parent}. Unhandled keyboard events will bubble to {@code parent}.
   *
   * @param parent the parent
   * @see #Component()
   * @see #handleKey(char)
   */
  public Component(Component parent) {
    setParent(parent);
  }

  /**
   * Loops until the {@code Component} returned by {@code componentFn} is rejected or is null. {@code handler} will
   * be executed each time the {@code Component} is accepted.
   *
   * @param <R>         the {@code ResultType} of the {@code Component} returned by {@code componentFn}
   * @param componentFn a function returning a {@code Component}
   * @param handler     a handler which is called each time the {@code Component} is accepted
   */
  public static <R> void loop(Supplier<Component<R>> componentFn, Consumer<R> handler) {
    for (; ; ) {
      Component<R> component = componentFn.get();
      if (component == null) {
        return;
      }

      Optional<R> result = componentFn.get().exec();
      if (!result.isPresent()) {
        return;
      }
      handler.accept(result.get());
    }
  }

  /**
   * Overloaded version of {@link #loop(Supplier, Consumer)} where {@code component} is fixed.
   */
  public static <R> void loop(Component<R> component, Consumer<R> handler) {
    loop(() -> component, handler);
  }

  /**
   * Returns the parent of this {@code Component}.
   *
   * @return the parent
   */
  public Component getParent() {
    return parent;
  }

  /**
   * Sets the parent of this {@code Component}.
   *
   * @param parent the parent
   */
  public void setParent(Component parent) {
    if (this.parent != null) { //abandon this
      this.parent.children.remove(this);
    }

    if (parent != null) { //adopt this
      parent.children.add(this);
    }

    this.parent = parent;
  }

  /**
   * This handler can be reimplemented in a subclass to receive non-reject
   * key event for this {@code Component}.
   * If the subclass can handle the event, it should return {@code true}, otherwise the event will bubble to its parent.
   * <p>
   * The default implementation does not handle the key and returns {@code false}.
   *
   * @param key the key
   * @return {@code true} if the event is handled, {@code false} otherwise.
   */
  protected boolean handleKey(char key) {
    return false;
  }

  /**
   * Convenient method, equivalent to {@code setResult(null);}
   *
   * @see #setResult(Object)
   */
  protected final void reject() {
    setResult(null);
  }

  /**
   * Sets the {@code result} of executing this {@code Component}.
   * <p>
   * If {@code result} is null, this {@code Component} is considered rejected.
   *
   * @param result the result
   */
  protected final void setResult(ResultType result) {
    this.result = Optional.ofNullable(result);
    finishedListener.accept(result);
  }

  /**
   * The method will be called right before this {@code Component} enters the keyboard loop.
   * Subclasses can re-implement this method to render itself and/or as an opportunity to stop the loop before
   * it is entered by setting the result of this {@code Component}.
   * <p>
   * The default implementation invokes {@link Renderer#render()}.
   *
   * @see #setResult(Object)
   */
  protected void willExec() {
    renderer.render();
  }

  private boolean propagateKeyEvent(char key) {
    boolean handled = false;
    for (int i = 0; i < children.size() && !handled; i++) { //let children handle first
      handled = children.get(i).propagateKeyEvent(key);
    }

    if (!handled) {
      handled = handleKey(key);
    }

    return handled;
  }

  /**
   * Executes the keyboard loop for this component. This is a blocking operation.
   *
   * @return The result of executing this {@code Component} or an empty {@link Optional} if this {@code Component}
   * is rejected.
   */
  public final Optional<ResultType> exec() {
    result = null;

    willExec();

    for (; ; ) {
      if (result != null) {
        return result;
      }

      char key = Console.waitKey();
      if (key == REJECTING_KEY) reject();
      else propagateKeyEvent(key);
    }
  }

  /**
   * Stops the keyboard loop and clears the area rendered by this {@code Component}.
   * Note that the finished signal will NOT be fired.
   */
  @Override
  public void close() {
    finishedListener = r -> {
    }; //disable finished listener
    reject(); //stop loop
    children.stream().forEach(child -> child.setParent(parent)); //reset children's parent
    setParent(null); //abandon this
    renderer.clear();
  }

  /**
   * Renders this {@code Component}.
   * Default implementation renders all children of this {@code Component}
   */
  public void doRender() {
    children.stream().forEach(Component::render);
  }

  /**
   * Clears the rendered area (if any) and re-renders this {@code Component}.
   */
  @Override
  public final void render() {
    renderer.render();
  }

  /**
   * Executes {@code listener} when the result of this {@code Component} is set.
   *
   * @param listener the listener
   */
  public void onFinished(Consumer<ResultType> listener) {
    finishedListener = listener;
  }
}
