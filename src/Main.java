import model.Board;
import model.GameModel;
import views.TerminalView;
import controller.GameController;

public class Main {
  public static void main(String[] args) {
    Board board = new Board();
    GameModel gm = new GameModel(board,5);
    GameController controller = new GameController(gm);
    TerminalView terminalView = new TerminalView(gm);

    gm.addObserver(terminalView);

    board.randomizeBoard();
    terminalView.run();
  }
}

