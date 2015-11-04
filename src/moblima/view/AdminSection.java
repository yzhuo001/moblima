package moblima.view;

import jni.Color;
import jni.Console;
import jni.TextColor;
import moblima.model.Database;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;
import moblima.view.movie.MovieManager;
import moblima.view.showtime.ShowTimeManager;

import java.util.Optional;

public class AdminSection extends Component {
	private final Database db;
	private SingleMenu menu;

	public AdminSection(Component parent, Database db) {
		super(parent);
		this.db = db;
	}

	private void makeMenu() {
		menu = new SingleMenu(
			this,
			(choice, ignored) -> {
				switch (choice) {
					case 0:
						new MovieManager(this, db.getMovieDb()).renderFocus();
						break;

					case 1:
						new ShowTimeManager(this, db.getShowTimeDb(), db.getCineplexes(), db.getMovieDb()).renderFocus();
						break;

					case 2:
						new PriceManager(this, db.getTicketPrice()).renderFocus();
						break;
				}
			},
			MenuStr.Orientation.Vertical,
			"Select an action you want to perform: ",
			"Create/update/remove movie listing",
			"Create/update/remove showtime",
			"Configure system settings"
		);
		attach(menu);
	}

	@Override
	public void onFocus() {
		if (menu != null) {
			menu.setFocus();
			return;
		}
		Console.clear();
		System.out.println("Please enter your username and password.");
		for (; ; ) {
			Optional<Boolean> result = InterruptibleScanner.nextLine("Username: ").flatMap(
				username -> InterruptibleScanner.nextPassword("Password: ").map(password -> db.getAdmin().verifyAdmin(username, password))
			);

			if (!result.isPresent()) { //interrupted
				cancel();
				return;
			}
			Console.clear();

			if (result.get()) {
				makeMenu();
				menu.renderFocus();
				return;
			} else {
				try (TextColor ignored = new TextColor(Color.RED)) {
					System.out.println("Invalid username or password, please try again!");
				}
			}
		}
	}
}
