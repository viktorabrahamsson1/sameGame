import model.GameBoard;
import views.Gui;

public class Main{
  public static void main(String[] args) {
    GameBoard gameBoard = new GameBoard();
    gameBoard.randomizeBoard();
    gameBoard.printBoard();


  }
}