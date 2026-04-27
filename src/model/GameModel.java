package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class GameModel {
  private final Board board;
  private GameState gameState;
  private int numberOfColors;
  List<Observer> observers;

  public GameModel(Board board,int numberOfColors) {
    this.board = board;
    this.gameState = GameState.PLAYING;
    this.numberOfColors = numberOfColors;
  }

  public Board getBoard() {
    return this.board;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  private ArrayList<Tile> findConnectedTiles(Tile tile){
    if(tile == null)
      throw new NullPointerException();

    Color color = tile.getColor();
    ArrayList<Tile> tiles = new ArrayList<>();


    return tiles;
  }

  private void collapseBoard(){

  }

}
