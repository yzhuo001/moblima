package moblima.model;

import moblima.util.Enums;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class TicketPrice implements Serializable {
	private int basePrice;
	private HashMap<Multiplier, Float> multipliers = new HashMap<>();
	private HashSet<LocalDate> holidays = new HashSet<>();

	public enum Multiplier {
		CHILDREN,
		SENIOR_CITIZEN,
		WEEKEND,
		AFTER_6,
		PUBLIC_HOLIDAY,
		PLATINUM_CINEMA,
		BLOCKBUSTER,
		THREE_DIMENSION;

		@Override
		public String toString() {
			switch (this) {
				case AFTER_6:
					return "After 18:00";

				case THREE_DIMENSION:
					return "3D";

				default:
					return Enums.format(super.toString());
			}
		}
	}

	public TicketPrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public float getMultiplier(Multiplier multiplier) {
		return multipliers.getOrDefault(multiplier, 1.f);
	}

	public void setMultiplier(Multiplier multiplier, float value) {
		multipliers.put(multiplier, value);
	}

	public HashSet<LocalDate> getHolidays() {
		return holidays;
	}
}
