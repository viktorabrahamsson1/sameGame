import model.Board;
import model.GameModel;
import model.Tile;
import views.GuiView;
import views.TerminalView;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    // OBSERVERS
    GuiView guiView = new GuiView();
    TerminalView terminalView = new TerminalView();

    // BOARD
    Board board = new Board();
    GameModel gm = new GameModel(board,5);
    gm.addObserver(terminalView);
    gm.addObserver(guiView);
    board.randomizeBoard();

    Scanner scanner = new Scanner(System.in);
    System.out.println("SameGame terminal test");
    System.out.println("Write a move as: row col");
    System.out.println("Rows: 0-" + (board.board.length - 1) + ", columns: 0-" + (board.board[0].length - 1));
    System.out.println("Only groups with 2 or more connected tiles can be removed.");
    System.out.println("Write q to quit.");

    while(true){
      printBoard(board);
      System.out.print("Move> ");
      String input = scanner.nextLine().trim();

      if(input.equalsIgnoreCase("q"))
        break;

      String[] parts = input.split("\\s+");
      if(parts.length != 2){
        System.out.println("Invalid input. Example: 4 7");
        continue;
      }

      try{
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        if(!board.isValidPosition(row, col)){
          System.out.println("Position is outside the board.");
          continue;
        }

        Tile selectedTile = board.getTile(row, col);
        if(selectedTile == null){
          System.out.println("That position is empty.");
          continue;
        }

        ArrayList<Tile> connectedTiles = gm.findConnectedTiles(selectedTile);
        System.out.println("Selected " + getTileSymbol(selectedTile) + " at row " + row + ", col " + col);
        System.out.println("Connected group size: " + connectedTiles.size());

        if(connectedTiles.size() < 2){
          System.out.println("Invalid move. Choose a group with at least 2 connected tiles.");
          continue;
        }

        gm.removeConnectedTiles(connectedTiles);
        System.out.println("Removed group. Board collapsed vertically and horizontally.");
      } catch(NumberFormatException e){
        System.out.println("Row and column must be numbers.");
      }
    }

    scanner.close();
    System.out.println("Bye!");
  }

  private static void printBoard(Board board){
    System.out.print("    ");
    for(int col = 0; col < board.board[0].length; col++){
      System.out.printf("%2d ", col);
    }
    System.out.println();

    for(int row = 0; row < board.board.length; row++){
      System.out.printf("%2d: ", row);

      for(int col = 0; col < board.board[row].length; col++){
        System.out.print(" " + getTileSymbol(board.getTile(row, col)) + " ");
      }

      System.out.println();
    }

    System.out.println();
  }

  private static String getTileSymbol(Tile tile){
    if(tile == null)
      return ".";

    return switch(tile.getColor()){
      case Black -> "K";
      case Blue -> "B";
      case Orange -> "O";
      case Yellow -> "Y";
      case Red -> "R";
    };
  }
}
