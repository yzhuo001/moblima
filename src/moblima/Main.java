package moblima;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import jni.Console;
import moblima.model.Database;
import moblima.view.Home;
import moblima.view.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The entry point for the application
 */
public class Main {
  private static final String dbFilename = "db.json";

  private static Gson makeGson() {
    return new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
      .enableComplexMapKeySerialization().create();
  }

  private static void saveDb(Database db) {
    try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dbFilename), "UTF-8"))) {
      out.write(makeGson().toJson(db));
    } catch (IOException e) {
    }
  }

  private static Database readDb() throws IOException {
    String fileContent = new String(Files.readAllBytes(Paths.get(dbFilename)), "UTF-8");
    Database db = makeGson().fromJson(fileContent, Database.class);
    if (db != null) {
      db.wakeUp();
    }
    return db;
  }


  /**
   * Loads the stored database and displays {@link Home} screen.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    Database db = null;

    try {
      db = readDb();
    } catch (IOException | JsonParseException e) {
      Util.pause("Database file is corrupted or not found and will be replaced", true);
    }

    if (db == null) {
      db = new Database();
    }

    //save DB when user closes console window
    final Database finalDb = db;
    Console.onClose(() -> saveDb(finalDb));

    Home.exec(db);
    saveDb(db);
  }
}
