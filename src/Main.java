import model.Board;

public class Main{
  public static void main(String[] args) {
    Board gameBoard = new Board();
    gameBoard.randomizeBoard();
    gameBoard.printBoard();


  }
}