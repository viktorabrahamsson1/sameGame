package controller;

import model.Board;
import model.GameModel;
import model.enums.GameState;
import model.Tile;

import java.util.ArrayList;

/**
 * Handles player input and translates it into actions on the game model.
 * This class is the Controller in the MVC design.
 */
public class GameController {
  private final GameModel model;

  /**
   * Creates a controller for the given game model.
   *
   * @param model the game model that should be controlled
   */
  public GameController(GameModel model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }

    this.model = model;
  }

  /**
   * Tries to perform a move at the selected board position.
   *
   * @param row the selected row
   * @param col the selected column
   * @return true if a valid group was removed, otherwise false
   */
  public boolean playMove(int row, int col) {
    if (model.getGameState() != GameState.PLAYING) {
      return false;
    }

    Board board = model.getBoard();

    if (!board.isValidPosition(row, col)) {
      return false;
    }

    Tile selectedTile = board.getTile(row, col);

    if (selectedTile == null) {
      return false;
    }

    ArrayList<Tile> connectedTiles = model.findConnectedTiles(selectedTile);

    if (connectedTiles.size() < 2) {
      return false;
    }

    model.removeConnectedTiles(connectedTiles);
    return true;
  }


  public void startNewGame(int difficultyLevel) {
    model.startNewGame(difficultyLevel);
  }
}
