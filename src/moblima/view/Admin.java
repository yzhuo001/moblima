package moblima.view;

import jni.Console;
import moblima.model.Database;
import moblima.view.Input;

public class Admin implements Component {
    Database db;

    public Admin(Database db) {
        this.db = db;
    }

    public void render() {
        Console.clear();
        System.out.println("Please enter your username and password.");
        System.out.println("Username: ");
        String username = Input.sc.nextLine();
        System.out.println("Password: ");
        String password = new String(System.console().readPassword());
    }
}
