package moblima.model;

import moblima.util.IntArray2D;

import java.io.Serializable;
import java.util.Optional;

public class Cinema implements Serializable {
	private static final int P = -1; //padding

	public static final IntArray2D LAYOUT = new IntArray2D(new int[] {
		0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
		2, P, 2, P, 2, P, 2, P, 0, 2, P, 2, P, 2, P, 2, P,
		2, P, 2, P, 2, P, 2, P, 0, 2, P, 2, P, 2, P, 2, P,
		2, P, 2, P, 2, P, 2, P, 0, 0, 0, 1, 1, 2, P, 2, P,
	}, 17);

	private static Integer seatCount;

	private int id;
	private Rank rank;
	private String name;

	public int getId() {
		return id;
	}

	public Rank getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static int getSeatCount() {
		if (seatCount == null) {
			seatCount = 0;
			for (int i = 0; i < LAYOUT.length(); ) {
				if (LAYOUT.at(i) > 0) {
					++seatCount;
					i += LAYOUT.at(i);
				} else {
					++i;
				}
			}
		}

		return seatCount;
	}

	enum Rank {
		PLATINUM,
		NORMAL
	}
}
