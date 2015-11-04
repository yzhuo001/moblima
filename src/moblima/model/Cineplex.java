package moblima.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Cineplex implements Serializable {
	private int id;
	private List<Cinema> cinemas = new ArrayList<>();
	private String name;

	public int getId() {
		return id;
	}

	public List<Cinema> getCinemas() {
		return cinemas;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
}
