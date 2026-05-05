package model;


/**
 * contains information about the best suggested move.
 */
public class MoveSuggestion {
  private final int row;
  private final int col;
  private final int groupSize;
  private final int points;

  /**
   *
   * @param row the row index of a tile
   * @param col the col index of a tile
   * @param groupSize the size of connected tiles from row and col indexes
   */
  public MoveSuggestion(int row, int col, int groupSize) {
    this.row = row;
    this.col = col;
    this.groupSize = groupSize;
    this.points = (int) Math.pow((groupSize - 2), 2);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getGroupSize() {
    return groupSize;
  }

  public int getPoints() {
    return points;
  }

}
