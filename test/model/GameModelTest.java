package model;

import model.enums.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
  @Test
  void findConnectedTilesFindsOnlyHorizontalAndVerticalMatches() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);
    Tile startTile = new Tile(Color.Red);

    board.setTile(9, 0, startTile);
    board.setTile(9, 1, new Tile(Color.Red));
    board.setTile(8, 1, new Tile(Color.Red));
    board.setTile(8, 0, new Tile(Color.Blue));
    board.setTile(7, 2, new Tile(Color.Red));

    ArrayList<Tile> connectedTiles = model.findConnectedTiles(startTile);

    assertEquals(3, connectedTiles.size());
    assertTrue(connectedTiles.contains(startTile));
  }

  @Test
  void removeConnectedTilesCollapsesBoardDownAndLeft() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);
    Tile firstTileToRemove = new Tile(Color.Red);
    Tile secondTileToRemove = new Tile(Color.Red);

    board.setTile(8, 0, firstTileToRemove);
    board.setTile(9, 0, secondTileToRemove);
    board.setTile(7, 1, new Tile(Color.Orange));
    board.setTile(9, 1, new Tile(Color.Yellow));
    board.setTile(8, 2, new Tile(Color.Blue));
    board.setTile(9, 2, new Tile(Color.Blue));

    model.removeConnectedTiles(model.findConnectedTiles(firstTileToRemove));

    assertNull(board.getTile(8, 2));
    assertNull(board.getTile(9, 2));
    assertEquals(Color.Orange, board.getTile(8, 0).getColor());
    assertEquals(Color.Yellow, board.getTile(9, 0).getColor());
    assertEquals(Color.Blue, board.getTile(8, 1).getColor());
    assertEquals(Color.Blue, board.getTile(9, 1).getColor());
  }

  @Test
  void getBestMoveSuggestionReturnsLargestCurrentGroup() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);

    board.setTile(9, 0, new Tile(Color.Red));
    board.setTile(9, 1, new Tile(Color.Red));
    board.setTile(9, 3, new Tile(Color.Blue));
    board.setTile(8, 3, new Tile(Color.Blue));
    board.setTile(8, 4, new Tile(Color.Blue));

    MoveSuggestion suggestion = model.getBestMoveSuggestion();

    assertNotNull(suggestion);
    assertEquals(8, suggestion.getRow());
    assertEquals(3, suggestion.getCol());
    assertEquals(3, suggestion.getGroupSize());
    assertEquals(9, suggestion.getPoints());
  }

  @Test
  void getBestMoveSuggestionReturnsNullWhenNoValidMoveExists() {
    Board board = new Board();
    GameModel model = new GameModel(board, 5);

    board.setTile(9, 0, new Tile(Color.Red));
    board.setTile(9, 1, new Tile(Color.Blue));

    assertNull(model.getBestMoveSuggestion());
  }
}
