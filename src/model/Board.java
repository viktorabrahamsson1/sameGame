package model;


import java.util.ArrayList;

public class Board {
  private final int COLUMNSIZE = 20;
  private final int ROWSIZE = 10;
  private final Tile[][] board;

  public Board(){
    this.board = new Tile[this.ROWSIZE][this.COLUMNSIZE];
  }

  public int getRowSize(){
    return this.board.length;
  }

  public int getColumnSize(){
    return this.board[0].length;
  }

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

  public ArrayList<Tile> getRows(int row){
    if(!isValidPosition(row,0))
      throw new IllegalArgumentException("Invalid row");
    ArrayList<Tile> tiles = new ArrayList<>();
    for(int i = 0; i < this.COLUMNSIZE; i++){
      tiles.add(this.board[row][i]);
    }
    return tiles;
  }

  public ArrayList<Tile> getCols(int col){
    if(!isValidPosition(0,col))
      throw new IllegalArgumentException("Invalid col");

    ArrayList<Tile> tiles = new ArrayList<>();
    for(int i = 0; i < this.ROWSIZE; i++){
      tiles.add(this.board[i][col]);
    }
    return tiles;
  }

  public Tile clearTile(int row, int col){
    if(!isValidPosition(row,col))
      throw new IllegalArgumentException("Invalid row");
    Tile tile = this.board[row][col];
    this.board[row][col] = null;
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

        if(j == 10){
          Tile tile = new Tile(Color.Red,2);
          this.board[i][j] = tile;
          continue;
        }
        Tile tile = new Tile(color, 1);
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
    return col >= 0 && col < this.COLUMNSIZE && row >= 0 && row < ROWSIZE;
  }

}
