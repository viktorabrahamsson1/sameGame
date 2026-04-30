package model;

/**
 * An observer interface which the observers need to implement
 */
public interface GameObserver {
  void updateBoard(Board board);
}
