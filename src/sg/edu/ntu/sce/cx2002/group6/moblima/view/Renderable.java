package sg.edu.ntu.sce.cx2002.group6.moblima.view;

/**
 * The {@code Renderable} interface represents minimum requirement for a view object.
 * A view object can be requested to render itself on the screen through calls to {@link #render()}.
 */
@FunctionalInterface
public interface Renderable {
  /**
   * Renders the {@code Renderable} on the screen.
   * This method should be pure, meaning that it does not modify the object state.
   */
  void render();
}
