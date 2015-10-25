package moblima.view;

import jni.Color;
import jni.TextColor;
import moblima.model.Database;

public class App implements Component {
    Database db;

    public App(Database db) {
        this.db = db;
    }

    public void render() {
        try (TextColor ignored =  new TextColor(Color.RED)) {
            System.out.println("==========================================================");
            System.out.println("Group 6 - Movie Booking and Listing Management Application");
            System.out.println("==========================================================");
        }

        Input.option(Input.Orientation.Horizontal, "Select module: ", "Admin", "Movie Goer")
        .ifPresent(option -> (option == 0 ? new Admin(db) : new MovieGoer(db)).render());
    }
}
