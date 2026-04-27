package model;

import java.util.ArrayDeque;
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

  public ArrayList<Tile> findConnectedTiles(Tile tile){
    ArrayList<int[]> connectedPositions = findConnectedTilePositions(tile);
    ArrayList<Tile> tiles = new ArrayList<>();

    for(int[] position : connectedPositions){
      tiles.add(this.board.getTile(position[0], position[1]));
    }

    return tiles;
  }


  private ArrayList<int[]> findConnectedTilePositions(Tile tile){
    if(tile == null)
      throw new NullPointerException("tile cant be null");

    int[] startPosition = findTilePosition(tile);
    if(startPosition == null)
      throw new IllegalArgumentException("tile does not exist on this board");

    Color targetColor = tile.getColor();
    ArrayList<int[]> connectedPositions = new ArrayList<>();
    boolean[][] visited = new boolean[this.board.board.length][this.board.board[0].length];
    ArrayDeque<int[]> positionsToCheck = new ArrayDeque<>();

    positionsToCheck.add(startPosition);
    visited[startPosition[0]][startPosition[1]] = true;

    while(!positionsToCheck.isEmpty()){
      int[] position = positionsToCheck.removeFirst();
      int row = position[0];
      int col = position[1];
      Tile currentTile = this.board.getTile(row, col);

      if(currentTile == null || currentTile.getColor() != targetColor)
        continue;

      connectedPositions.add(position);

      addPositionIfValid(row - 1, col, visited, positionsToCheck);
      addPositionIfValid(row + 1, col, visited, positionsToCheck);
      addPositionIfValid(row, col - 1, visited, positionsToCheck);
      addPositionIfValid(row, col + 1, visited, positionsToCheck);
    }

    return connectedPositions;
  }

  private int[] findTilePosition(Tile tile){
    for(int row = 0; row < this.board.board.length; row++){
      for(int col = 0; col < this.board.board[row].length; col++){
        if(this.board.getTile(row, col) == tile)
          return new int[]{row, col};
      }
    }

    return null;
  }

  private void addPositionIfValid(int row, int col, boolean[][] visited, ArrayDeque<int[]> positionsToCheck){
    if(!this.board.isValidPosition(row, col) || visited[row][col])
      return;

    visited[row][col] = true;
    positionsToCheck.add(new int[]{row, col});
  }




}
