package model;


import java.util.ArrayList;

public class GameBoard {
  private final int COLUMNSIZE = 20;
  private final int ROWSIZE = 10;
  public Tile[][] board;

  public GameBoard(){
    this.board = new Tile[this.ROWSIZE][this.COLUMNSIZE];
  }


  public Tile getTile(int row, int col){
    Tile tile = null;
    if(isValidPosition(row,col)){
      tile = this.board[row][col];
    }
    return tile;
  }

  public void randomizeBoard(){

    for(int i = 0; i < this.ROWSIZE; i++){
      for(int j = 0; j < this.COLUMNSIZE; j++){
        int randColor = (int) (Math.random() * 5);
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

  public void printBoard(){
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < this.ROWSIZE; i++){
      for(int j = 0; j < this.COLUMNSIZE; j++){
        Tile tile = this.board[i][j];
        sb.append(tile.getColor()).append(", ");
      }
      sb.append("\n");
    }

    System.out.println(sb);
  }


  public boolean isValidPosition(int row, int col){
    return col >= 0 && col <= this.COLUMNSIZE && row >= 0 && row <= ROWSIZE;
  }

}
