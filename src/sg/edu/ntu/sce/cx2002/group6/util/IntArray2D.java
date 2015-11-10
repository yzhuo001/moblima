package sg.edu.ntu.sce.cx2002.group6.util;

import java.util.Arrays;

/**
 * A wrapper which provides 2D array operations on a 1D array.
 */
public class IntArray2D {
  private int[] arr;
  private int rowSize;

  /**
   * Constructs a new 2D array from a 1D array.
   *
   * @param arr     the 1D array
   * @param rowSize the size of each row
   */
  public IntArray2D(int[] arr, int rowSize) {
    this.arr = arr;
    this.rowSize = rowSize;
  }

  /**
   * Translates the index in a 1D array to row and column values in the corresponding 2D array.
   *
   * @param index   the index in the 1D array
   * @param rowSize the size of each row in the 2D array
   * @return the (row, col) pair
   */
  public static Pair<Integer, Integer> indexToRC(int index, int rowSize) {
    int row = index / rowSize;
    int col = index % rowSize;
    return new Pair<>(row, col);
  }

  /**
   * Translates the row and column values in a 2D array to the index in the corresponding 1D array.
   *
   * @param row     the row
   * @param col     the col
   * @param rowSize the size of each row in the 2D array
   * @return the index
   */
  public static int rcToIndex(int row, int col, int rowSize) {
    return row * rowSize + col;
  }

  /**
   * Translates the index the associated 1D array to row and column values in this 2D array.
   *
   * @param index the index
   * @return the (row, col) pair
   */
  public Pair<Integer, Integer> indexToRC(int index) {
    int row = index / rowSize;
    int col = index % rowSize;
    return new Pair<>(row, col);
  }

  /**
   * Returns the original 1D array.
   *
   * @return the 1D array
   */
  public int[] array() {
    return arr;
  }

  /**
   * Returns the size of each row in this 2D array.
   *
   * @return the size of each row
   */
  public int rowSize() {
    return rowSize;
  }

  /**
   * Returns the element at the given 2D position.
   *
   * @param row the row
   * @param col the col
   * @return the element
   */
  public int at(int row, int col) {
    return arr[row * rowSize + col];
  }

  /**
   * Returns the element at the given 1D index.
   *
   * @param index the index
   * @return the element
   */
  public int at(int index) {
    return arr[index];
  }

  /**
   * Returns the number of rows in this 2D array.
   *
   * @return the int
   */
  public int rowCount() {
    return arr.length / rowSize;
  }

  /**
   * Returns the length of the 1D Array.
   *
   * @return the length
   */
  public int length() {
    return arr.length;
  }

  /**
   * Returns the nth row in this 2D array.
   *
   * @param row the row index
   * @return the array representing the row
   */
  public int[] row(int row) {
    return Arrays.copyOfRange(arr, row * rowSize, (row + 1) * rowSize);
  }

  /**
   * Translates the row and column values in this 2D array to the index in the associated 1D array.
   *
   * @param row the row
   * @param col the col
   * @return the index
   */
  public int rcToIndex(int row, int col) {
    return rcToIndex(row, col, rowSize);
  }
}
