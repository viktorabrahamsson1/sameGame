package model;


public class MoveSuggestion {
  private final int row;
  private final int col;
  private final int groupSize;
  private final int points;

  public MoveSuggestion(int row, int col, int groupSize) {
    this.row = row;
    this.col = col;
    this.groupSize = groupSize;
    this.points = this.groupSize * this.groupSize;
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
