package moblima.view.movie;

import moblima.model.Movie;
import moblima.view.Component;
import moblima.view.menu.PagedMenu;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MovieMenu extends Component {
	private final int ITEMS_PER_PAGE = 4;
	private final PagedMenu menu;

	public MovieMenu(Component parent, Consumer<Movie> onAccept, Stream<Movie> movies) {
		super(parent);
		menu = new PagedMenu(
			this,
			(ignored, item) -> onAccept.accept(((MovieItem) item).getMovie()),
			ITEMS_PER_PAGE,
			movies.map(MovieItem::new).toArray(MovieItem[]::new)
		);
		attach(menu);
	}

	public Movie getActiveMovie() {
		return ((MovieItem) menu.getActiveOption()).getMovie();
	}
}
