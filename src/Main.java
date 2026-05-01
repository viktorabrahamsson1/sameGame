import model.*;
import views.TerminalView;
import views.GuiView;
import controller.GameController;

public class Main {
  public static void main(String[] args) {
    Board board = new Board();
    GameModel gm = new GameModel(board, 5);
    GameController controller = new GameController(gm);

    TerminalView terminalView = new TerminalView(gm, controller);
    GuiView guiView = new GuiView(gm, controller);

    TileClearSound tileClearSound = new TileClearSound();
    LostSound lostSound = new LostSound();
    WinSound winSound = new WinSound();

    gm.addObserver(terminalView);
    gm.addObserver(guiView);

    gm.addSoundObserver(lostSound);
    gm.addSoundObserver(tileClearSound);
    gm.addSoundObserver(winSound);

    gm.startNewGame();
    
    terminalView.run();
  }
}