package model;

import model.enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
  @Test
  void getAndSetTileUseValidBoardPositions() {
    Board board = new Board();
    Tile tile = new Tile(Color.Blue);

    board.setTile(0, 0, tile);

    assertSame(tile, board.getTile(0, 0));
    assertNull(board.getTile(-1, 0));
    assertNull(board.getTile(0, 20));
  }

  @Test
  void isValidPositionChecksBoardBounds() {
    Board board = new Board();

    assertTrue(board.isValidPosition(0, 0));
    assertTrue(board.isValidPosition(9, 19));
    assertFalse(board.isValidPosition(-1, 0));
    assertFalse(board.isValidPosition(0, -1));
    assertFalse(board.isValidPosition(10, 0));
    assertFalse(board.isValidPosition(0, 20));
  }

  @Test
  void setNumberOfColorsOnlyAcceptsDifficultyRangeTwoToFive() {
    Board board = new Board();

    assertDoesNotThrow(() -> board.setNumberOfColors(2));
    assertDoesNotThrow(() -> board.setNumberOfColors(5));
    assertThrows(IllegalArgumentException.class, () -> board.setNumberOfColors(1));
    assertThrows(IllegalArgumentException.class, () -> board.setNumberOfColors(6));
  }
}
