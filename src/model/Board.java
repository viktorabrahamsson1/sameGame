package model;

import model.enums.Color;

/**
 * Represents the board of the sameGame and contains a column size, row size, board and
 * number of colors in the board.
 */

public class Board {
  private final int COLUMNSIZE = 20;
  private final int ROWSIZE = 10;
  private final Tile[][] board;
  private int numberOfColors;

  /**
   * Initialize a 2D array of Tiles that represents the board
   */
  public Board(){
    this.board = new Tile[this.ROWSIZE][this.COLUMNSIZE];
  }

  public int getRowSize(){return this.board.length;}
  public int getColumnSize(){return this.board[0].length;}
  public void setNumberOfColors(int num){
    if(num < 2 || num > 5)
      throw new IllegalArgumentException("amount of colors must be between 2 and 5");

    this.numberOfColors = num;
  }

  /**
   *
   * @param row the row index to locate
   * @param col the column index to locate
   * @return A tile from the board or null if the tile is not valid
   */

  public Tile getTile(int row, int col){
    Tile tile = null;
    if(isValidPosition(row,col)){
      tile = this.board[row][col];
    }
    return tile;
  }

  public void setTile(int row, int col, Tile tile){

    if(isValidPosition(row,col)){
      this.board[row][col] = tile;
    }
  }

  /**
   * Goes over the board and creates a new Tile with a "random" color
   * @throws IllegalStateException if the random number is unexpected
   */
  public void randomizeBoard(){

    for(int i = 0; i < this.ROWSIZE; i++){
      for(int j = 0; j < this.COLUMNSIZE; j++){
        int randColor = (int) (Math.random() * this.numberOfColors);
        Color color = switch (randColor){
          case 0 -> Color.Black;
          case 1 -> Color.Blue;
          case 2 -> Color.Yellow;
          case 3 -> Color.Red;
          case 4 -> Color.Orange;
          default -> throw new IllegalStateException("Unexpected value: " + randColor);
        };

        Tile tile = new Tile(color);
        this.board[i][j] = tile;
      }
    }
  }

  /**
   *
   * @param row the row index to be checked if its valid
   * @param col the column index to be checked if its valid
   * @return checks if the inputs row and col are valid and returns true / false
   */

  public boolean isValidPosition(int row, int col){
    return col >= 0 && col < this.COLUMNSIZE && row >= 0 && row < ROWSIZE;
  }
}
