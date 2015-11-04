package moblima.view.movie;

import jni.Console;
import moblima.model.Movie;
import moblima.view.Component;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;

import java.util.function.Consumer;

public class MovieEdit extends Component {
	MovieInput input;

	SingleMenu menu = new SingleMenu(
		this,
		(choice, ignored) -> {
			Consumer<Runnable> method = null;
			switch (choice) {
				case 0:
					method = input::askTitle;
					break;
				case 1:
					method = input::askGenres;
					break;
				case 2:
					method = input::askClassification;
					break;
				case 3:
					method = input::askShowingStatus;
					break;
				case 4:
					method = input::askLength;
					break;
				case 5:
					method = input::askCasts;
					break;
				case 6:
					method = input::askDirector;
					break;
				case 7:
					method = input::askSynopsis;
					break;
				default:
					assert false;
			}
			method.accept(() -> {
				System.out.println("Saved! Press any key to continue...");
				Console.getChar();
				renderFocus();
			});
		},
		MenuStr.Orientation.Vertical,
		"",
		"Title",
		"Genres",
		"Classification",
		"Showing status",
		"Length",
		"Casts",
		"Director",
		"Synopsis"
	);

	public MovieEdit(Component parent, Movie movie) {
		super(parent);
		attach(menu);
		input = new MovieInput(movie, this);
	}

	@Override
	public void render() {
		Console.clear();
		System.out.printf("Editing %s. Select an item to edit: \n", input.getMovie().getTitle());
		menu.render();
	}
}
