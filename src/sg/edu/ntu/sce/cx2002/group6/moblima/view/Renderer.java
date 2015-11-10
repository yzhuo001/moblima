package sg.edu.ntu.sce.cx2002.group6.moblima.view;

import sg.edu.ntu.sce.cx2002.group6.console.Console;
import sg.edu.ntu.sce.cx2002.group6.util.Pair;

/**
 * A {@code Renderer} is a proxy for {@link Renderable}s which clears old rendering area of a {@link Renderable}
 * before it re-renders.
 */
public class Renderer implements Renderable {
  private Pair<Integer, Integer> start;
  private Pair<Integer, Integer> end;
  private Renderable r;

  /**
   * Constructs a {@code Renderer} associated with {@code r}
   *
   * @param r the associated {@code Renderable}
   */
  public Renderer(Renderable r) {
    this.r = r;
  }

  private void recordStart() {
    clear();
    start = Console.cursor();
  }

  /**
   * Clears the rendered area and re-renders the associated {@code Renderable}
   */
  @Override
  public void render() {
    recordStart();
    r.render();
    end = Console.cursor();
  }

  /**
   * Clears the rendered area (if any).
   */
  public void clear() {
    if (start != null) {
      Console.clear(start.first, start.second, -1, end.second - start.second + 1);
    }
  }

  /**
   * Returns the top-left coordinate of the rendering area or null if the associated {@link Renderable} is not rendered.
   *
   * @return (x, y) pair
   */
  public Pair<Integer, Integer> getStart() {
    return start;
  }
}
