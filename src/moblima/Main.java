package moblima;
import moblima.view.App;
import moblima.model.Database;


import java.io.*;

public class Main {
    static final String dbFilename = "db.bin";

    public static void saveDb(Database db) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dbFilename))) {
            outputStream.writeObject(db);
        }
    }

    public static Database readDb() throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dbFilename))) {
            return (Database) inputStream.readObject();
        }
    }

    public static void main(String[] args) {
        Database db = null;

        try {
            db = readDb();
        } catch (FileNotFoundException ignored) {

        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Database file is corrupted and will be replaced!");
        }


        if (db == null) {
            db = new Database();
        }

        new App(db).render();

        try {
            saveDb(db);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("Database file could not be created.");
        }
    }
}
