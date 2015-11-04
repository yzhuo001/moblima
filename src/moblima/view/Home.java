package moblima.view;

import jni.Color;
import jni.Console;
import jni.TextColor;
import moblima.model.Database;
import moblima.view.menu.MenuStr;
import moblima.view.menu.SingleMenu;

public class Home extends Component {
	private final SingleMenu menu;

	public Home(Database db) {
		super(null);
		menu = new SingleMenu(this,
			(choice, ignored) -> {
				switch (choice) {
					case 0:
						new AdminSection(this, db).renderFocus();
						break;

					case 1:
						new MovieGoerSection(this, db).renderFocus();
						break;
				}
			},
			MenuStr.Orientation.Horizontal,
			"Select module: ",
			"Admin",
			"Movie Goer"
		);
		attach(menu);
	}

	@Override
	public void render() {
		Console.clear();
		try (TextColor ignored = new TextColor(Color.RED)) {
			System.out.println("==================================================");
			System.out.println("Group 6 - Movie Booking and Listing Management App");
			System.out.println("==================================================");
		}
		menu.render();
	}
}
