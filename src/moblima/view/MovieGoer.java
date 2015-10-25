package moblima.view;

import jni.Console;
import moblima.model.Database;

public class MovieGoer implements Component {
    Database db;

    public MovieGoer(Database db) {
        this.db = db;
    }

    public void render() {
        Console.clear();
        Input.option(Input.Orientation.Vertical,
                "Select the action you want to perform: ",
                "View list of movies",
                "View your booking history");
    }
}
