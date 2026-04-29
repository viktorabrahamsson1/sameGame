package model;


public class Board {
  private final int COLUMNSIZE = 20;
  private final int ROWSIZE = 10;
  private final Tile[][] board;
  private int numberOfColors;

  public Board(){
    this.board = new Tile[this.ROWSIZE][this.COLUMNSIZE];
  }

  public int getRowSize(){return this.board.length;}
  public int getColumnSize(){return this.board[0].length;}
  public void setNumberOfColors(int num){
    if(num <= 0)
      throw new IllegalArgumentException("amount of colors cant be 0 or less");

    if(num < 6){
      this.numberOfColors = num;
    } else {
      this.numberOfColors = 5;
    }
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

        if(j == 10){
          Tile tile = new Tile(Color.Red);
          this.board[i][j] = tile;
          continue;
        }
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
    return col >= 0 && col < this.COLUMNSIZE && row >= 0 && row < ROWSIZE;
  }

  public boolean isEmpty() {
  for (int row = 0; row < ROWSIZE; row++) {
    for (int col = 0; col < COLUMNSIZE; col++) {
      if (board[row][col] != null) {
        return false;
      }
    }
  }
  return true;
}

}
