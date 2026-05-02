import model.*;
import model.sounds.LostSound;
import model.sounds.TileClearSound;
import model.sounds.WinSound;
import views.TerminalView;
import views.GuiView;
import controller.GameController;

import javax.swing.JOptionPane;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    int difficultyLevel = readDifficultyLevel();
    Board board = new Board();
    GameModel gm = new GameModel(board, difficultyLevel);
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

    gm.startNewGame(difficultyLevel);

    terminalView.run();
  }

  private static int readDifficultyLevel() {
    Integer difficultyLevel = readDifficultyLevelFromGui();

    if(difficultyLevel != null)
      return difficultyLevel;

    return readDifficultyLevelFromTerminal();
  }

  private static Integer readDifficultyLevelFromGui() {
    Integer[] difficultyLevels = {2, 3, 4, 5};

    try {
      return (Integer) JOptionPane.showInputDialog(
          null,
          "Choose difficulty level",
          "SameGame",
          JOptionPane.QUESTION_MESSAGE,
          null,
          difficultyLevels,
          5
      );
    } catch(Exception e) {
      return null;
    }
  }

  private static int readDifficultyLevelFromTerminal() {
    Scanner scanner = new Scanner(System.in);

    while(true) {
      System.out.print("Choose difficulty level (2-5 colors): ");
      String input = scanner.nextLine().trim();

      try {
        int difficultyLevel = Integer.parseInt(input);

        if(difficultyLevel >= 2 && difficultyLevel <= 5)
          return difficultyLevel;

        System.out.println("Difficulty must be between 2 and 5.");
      } catch(NumberFormatException e) {
        System.out.println("Write a number between 2 and 5.");
      }
    }
  }
}
