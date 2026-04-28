import model.Board;
import model.GameModel;

public class Main {
  public static void main(String[] args) {
    Board board = new Board();
    GameModel gm = new GameModel(board,5);
    board.randomizeBoard();
    board.printBoard();
  }
}
