package moblima.util;

import java.util.Arrays;

public class IntArray2D {
	private int[] arr;
	private int rowSize;

	public IntArray2D(int[] arr, int rowSize) {
		this.arr = arr;
		this.rowSize = rowSize;
	}

	public Pair<Integer, Integer> indexToRC(int index) {
		int row = index / rowSize;
		int col = index % rowSize;
		return new Pair<>(row, col);
	}

	public int[] getArr() {
		return arr;
	}

	public int getRowSize() {
		return rowSize;
	}

	public int get(int row, int col) {
		return arr[row * rowSize + col];
	}

	public int at(int index) {
		return arr[index];
	}

	public int rowCount() {
		return arr.length / rowSize;
	}

	public int length() {
		return arr.length;
	}

	public int[] getRow(int row) {
		return Arrays.copyOfRange(arr, row * rowSize, (row + 1) * rowSize);
	}

	public int rcToIndex(int row, int col) {
		return row * rowSize + col;
	}

	public static Pair<Integer, Integer> indexToRC(int index, int rowSize) {
		int row = index / rowSize;
		int col = index % rowSize;
		return new Pair<>(row, col);
	}

	public static int rcToIndex(int row, int col, int rowSize) {
		return row * rowSize + col;
	}
}
