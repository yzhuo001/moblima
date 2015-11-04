package moblima.util;

public class Calc {
	public static int clamp(int val, int left, int right) {
		return left + Math.floorMod(val, (right - left) + 1);
	}
}
