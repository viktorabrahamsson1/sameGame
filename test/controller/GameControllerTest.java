package controller;

import model.Board;
import model.GameModel;
import model.Tile;
import model.enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

  @Test
  void playMoveRemovesValidGroup() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);
    GameController controller = new GameController(model);

    board.setTile(9, 0, new Tile(Color.Red));
    board.setTile(9, 1, new Tile(Color.Red));
    board.setTile(9, 3, new Tile(Color.Blue));
    board.setTile(8, 3, new Tile(Color.Blue));

    assertTrue(controller.playMove(9, 0));
    assertEquals(4, model.getPoints());
    assertNull(board.getTile(9, 3));
    assertEquals(Color.Blue, board.getTile(9, 0).getColor());
    assertEquals(Color.Blue, board.getTile(8, 0).getColor());
  }

  @Test
  void playMoveRejectsSingletonTile() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);
    GameController controller = new GameController(model);
    Tile tile = new Tile(Color.Red);

    board.setTile(9, 0, tile);

    assertFalse(controller.playMove(9, 0));
    assertSame(tile, board.getTile(9, 0));
    assertEquals(0, model.getPoints());
  }

  @Test
  void startNewGameUsesSelectedDifficulty() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);
    GameController controller = new GameController(model);

    controller.startNewGame(2);

    assertEquals(2, model.getDifficultyLevel());
    assertEquals(0, model.getPoints());
  }
}
