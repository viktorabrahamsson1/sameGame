package model;
import controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {
  private Board board;
  private GameController controller;
  private GameModel model;

  @BeforeEach
  void setUp() {
    this.board = new Board();
    this.model = new GameModel(board, 5);
    this.controller = new GameController(this.model);
  }

  @Test
  void test() {
    Board board = new Board();
  }
}
