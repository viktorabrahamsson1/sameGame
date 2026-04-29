import model.Board;
import model.GameModel;
import views.TerminalView;
import views.GuiView;
import controller.GameController;

public class Main {
  public static void main(String[] args) {
    Board board = new Board();
    GameModel gm = new GameModel(board, 5);
    GameController controller = new GameController(gm);

    TerminalView terminalView = new TerminalView(gm, controller);
    GuiView guiView = new GuiView(controller);

    gm.addObserver(terminalView);
    gm.addObserver(guiView);

    gm.startNewGame();
    terminalView.run();
  }
}