package views;

import controller.GameController;
import model.Board;
import model.GameModel;
import model.GameObserver;
import model.Tile;

import java.util.Scanner;

public class TerminalView implements GameObserver {
  private final GameModel model;
  private final GameController controller;
  private final Scanner scanner;

  public TerminalView(GameModel model, GameController controller) {
    if (model == null || controller == null) {
      throw new IllegalArgumentException("model and controller cannot be null");
    }

    this.model = model;
    this.controller = controller;
    this.scanner = new Scanner(System.in);
  }

  public void run() {
    System.out.println("SameGame terminal test");
    System.out.println("Write a move as: row col");
    System.out.println("Write q to quit.");

    updateBoard(model.getBoard());

    while (true) {
      System.out.print("Move> ");
      String input = scanner.nextLine().trim();

      if (input.equalsIgnoreCase("q")) {
        break;
      }

      String[] parts = input.split("\\s+");

      if (parts.length != 2) {
        System.out.println("Invalid input. Example: 4 7");
        continue;
      }

      try {
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        boolean moveWasMade = controller.playMove(row, col);

        if (!moveWasMade) {
          System.out.println("Invalid move. Choose a group with at least 2 connected tiles.");
        }

      } catch (NumberFormatException e) {
        System.out.println("Row and column must be numbers.");
      }
    }

    scanner.close();
    System.out.println("Bye!");
  }

  @Override
  public void updateBoard(Board board) {
    printBoard(board);
  }

  private void printBoard(Board board) {
    System.out.print("    ");

    for (int row = 0; row < board.getRowSize(); row++) {
      System.out.printf("%2d ", col);
    }

    System.out.println();

    for (int row = 0; row < board.board.length; row++) {
      System.out.printf("%2d: ", row);

      for (int col = 0; col < board.getColumnSize(); col++) {
        System.out.print(" " + getTileSymbol(board.getTile(row, col)) + " ");
      }

      System.out.println();
    }

    System.out.println();
  }

  private String getTileSymbol(Tile tile) {
    if (tile == null) {
      return ".";
    }

    return switch (tile.getColor()) {
      case Black -> "K";
      case Blue -> "B";
      case Orange -> "O";
      case Yellow -> "Y";
      case Red -> "R";
    };
  }
}