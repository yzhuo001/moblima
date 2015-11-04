package moblima;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jni.Color;
import jni.Console;
import jni.TextColor;
import moblima.model.Cinema;
import moblima.model.Database;
import moblima.util.Strings;
import moblima.view.App;
import moblima.view.Home;
import moblima.view.SeatLayout;
import moblima.view.movie.MovieManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
	static final String dbFilename = "db.json";

	private static Gson makeGson() {
		return new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
			.enableComplexMapKeySerialization().create();
	}

	private static void saveDb(Database db) throws IOException {
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dbFilename), "UTF-8"))) {
			out.write(makeGson().toJson(db));
		}
	}

	public static Database readDb() throws IOException {
		String fileContent = new String(Files.readAllBytes(Paths.get(dbFilename)), "UTF-8");
		Database db = makeGson().fromJson(fileContent, Database.class);
		db.wakeUp();
		return db;
	}




	public static void main(String[] args) {
		Database db = null;

		try {
			db = readDb();
		} catch (IOException e) {
			System.err.println("Database file is corrupted or not found and will be replaced!\nPress any key to continue...");
			Console.getChar();
		}

		if (db == null) {
			db = new Database();
		}

		final Database finalDb = db;
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				saveDb(finalDb);
			} catch (IOException e) {
				try (TextColor ignore = new TextColor(Color.RED)) {
					System.err.println("Error saving database file. All data will be lost. Press any key to continue...");
				}

				Console.getChar();
			}
		}));

		//System.out.println("┌───┬───┬───┬───┐");
		//System.out.println("│   │   │   │   │");
		//System.out.println("├───┼───┼───┼───┤");
		//System.out.println("│   │   │   │   │");
		//System.out.println("└───┴───┴───┴───┘");
		//System.out.printf("%-3c", ' ');
		//System.out.print('a');

		//new SeatLayout(null, null, Arrays.stream(new int[]{2, 3, 4})).renderFocus();
		new Home(db).renderFocus();
		App.inst.run();
	}
}
